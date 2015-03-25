package pl.edu.agh.nlp.spark.algorithms;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import pl.edu.agh.nlp.spark.SparkContextFactory;
import scala.Tuple2;

public class CollaborativeFiltering {

	private static final String MODEL_PATH = "models/recomender/model.o";

	public MatrixFactorizationModel builidModel() {
		JavaSparkContext sc = SparkContextFactory.getJavaSparkContext();

		// Wczytanie danych
		JavaRDD<String> data = sc.textFile("u.data");

		JavaRDD<Rating> ratings = data.map(r -> {
			String[] sarray = r.split("\t");
			return new Rating(Integer.parseInt(sarray[0]), Integer.parseInt(sarray[1]), Double.parseDouble(sarray[2]));
		});
		// JavaRDD<Rating> ratings = JdbcRDD.create(jsc, new PostgresConnection(),
		// "select * from users_articles where  ? <= userId AND userId <= ?", 1, 1000, 2, new RatingMapper());
		// Budowa modelu
		int rank = 50;
		int numIterations = 10;
		MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(ratings), rank, numIterations, 0.01);
		return model;
	}

	public void saveModel(JavaSparkContext sc, MatrixFactorizationModel model) {
		model.save(sc.sc(), MODEL_PATH);
	}

	public void evaluateModel(JavaRDD<Rating> evaluateData, MatrixFactorizationModel model) {
		JavaRDD<Tuple2<Object, Object>> userProducts = evaluateData.map(r -> new Tuple2<Object, Object>(r.user(), r
				.product()));

		JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(model
				.predict(JavaRDD.toRDD(userProducts))
				.toJavaRDD()
				.map(r -> new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r
						.product()), r.rating())));

		JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD
				.fromJavaRDD(
						evaluateData.map(r -> new Tuple2<Tuple2<Integer, Integer>, Double>(
								new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating()))).join(predictions)
				.values();

		double MSE = JavaDoubleRDD.fromRDD(ratesAndPreds.map(pair -> {
			Double err = pair._1() - pair._2();
			return (Object) (err * err);
		}).rdd()).mean();
		System.out.println("Mean Squared Error = " + MSE);
	}

}