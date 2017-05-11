package pl.edu.agh.nlp.spark.algorithms.recommendations;

import lombok.extern.log4j.Log4j;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

@Service
@Log4j
public class CollaborativeFiltering {

	private MatrixFactorizationModel model;
	@Autowired
	private ArticlesReader articlesReader;
	private final static double[] splitTable = { 0.6, 0.2, 0.2 };
	private final static int NUMBER_OF_RECOMMENDATIONS = 10;

	public void buildModel() {
		JavaRDD<Rating> ratings = articlesReader.readArticlesHistoryToRDD();

		JavaRDD<Rating>[] splits = ratings.randomSplit(splitTable);
		JavaRDD<Rating> training = splits[0];
		training.cache();
		JavaRDD<Rating> test = splits[1];
		test.cache();
		JavaRDD<Rating> validation = splits[2];
		validation.cache();

		model = validateModels(training, validation);
		evaluateModel(model, test);
		log.info("Recommender training finshed");
	}

	private double evaluateModel(MatrixFactorizationModel model, JavaRDD<Rating> evaluateData) {
		JavaRDD<Tuple2<Object, Object>> userProducts = evaluateData.map(r -> new Tuple2<>(r.user(), r.product()));

		JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(model.predict(JavaRDD.toRDD(userProducts))
				.toJavaRDD()
				.map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())));

		JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD
				.fromJavaRDD(evaluateData.map(
						r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating())))
				.join(predictions).values();

		double RMSE = Math.sqrt(JavaDoubleRDD.fromRDD(ratesAndPreds.map(pair -> {
			Double err = pair._1() - pair._2();
			return (Object) (err * err);
		}).rdd()).mean());

		log.info("Root Mean Squared Error = " + RMSE);
		return RMSE;
	}

	private MatrixFactorizationModel validateModels(JavaRDD<Rating> training, JavaRDD<Rating> validation) {
		List<Integer> ranks = Arrays.asList(8, 12, 20, 50, 100, 200);
		List<Double> lambdas = Arrays.asList(0.01, 0.1, 1.0, 10.0);
		List<Integer> numIters = Arrays.asList(10, 20, 30, 40, 50);

		MatrixFactorizationModel bestModel = null;
		double bestValidationRmse = Double.MAX_VALUE;
		int bestRank = 0;
		double bestLambda = -1.0;
		int bestNumIter = -1;

		for (int rank : ranks) {
			for (double lambda : lambdas) {
				for (int numIter : numIters) {
					long time1 = System.currentTimeMillis();
					MatrixFactorizationModel validatedModel = ALS.train(JavaRDD.toRDD(training), rank, numIter, lambda);
					long time2 = System.currentTimeMillis();
					double validationRmse = evaluateModel(validatedModel, validation);
					log.info("RMSE (validation) = " + validationRmse + " for the model trained with rank = " + rank + ", lambda = "
							+ lambda + ", and numIter = " + numIter + ". Model training time " +  (time2 - time1) + "ms.");
					if (validationRmse < bestValidationRmse) {
						bestModel = validatedModel;
						bestValidationRmse = validationRmse;
						bestRank = rank;
						bestLambda = lambda;
						bestNumIter = numIter;
					}
				}
			}
		}
		log.info(
				"The best model was trained with rank = " + bestRank + " and lambda = " + bestLambda + ", and numIter = " + bestNumIter);
		return bestModel;
	}

	public Rating[] recommend(Integer userId){
		if (model != null)
			return model.recommendProducts(userId, NUMBER_OF_RECOMMENDATIONS);
		else
			throw new AbsentModelException();
	}

}