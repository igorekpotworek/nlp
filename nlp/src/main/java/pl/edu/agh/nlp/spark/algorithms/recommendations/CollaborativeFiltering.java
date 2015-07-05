package pl.edu.agh.nlp.spark.algorithms.recommendations;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import scala.Tuple2;

@Service
public class CollaborativeFiltering {

	private MatrixFactorizationModel model;
	@Autowired
	private ArticlesReader articlesReader;

	public void builidModel() {

		JavaRDD<Rating> ratings = articlesReader.readArticlesHistoryToRDD();
		// Budowa modelu
		// im wyzszy tym lepiej
		int rank = 50;
		// im wyzszy tym lepiej
		int numIterations = 10;
		model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations, 0.01);
		evaluateModel(ratings);

	}

	public void evaluateModel(JavaRDD<Rating> evaluateData) {
		JavaRDD<Tuple2<Object, Object>> userProducts = evaluateData.map(r -> new Tuple2<Object, Object>(r.user(), r.product()));

		JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(model.predict(JavaRDD.toRDD(userProducts))
				.toJavaRDD()
				.map(r -> new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating())));

		JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD
				.fromJavaRDD(
						evaluateData.map(r -> new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r
								.product()), r.rating()))).join(predictions).values();

		double MSE = JavaDoubleRDD.fromRDD(ratesAndPreds.map(pair -> {
			Double err = pair._1() - pair._2();
			return (Object) (err * err);
		}).rdd()).mean();
		System.out.println("Mean Squared Error = " + MSE);
	}

	public Rating[] recommend(Integer userId) {
		return model.recommendProducts(userId, 10);
	}

}