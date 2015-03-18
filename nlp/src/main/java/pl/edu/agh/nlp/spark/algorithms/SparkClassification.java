package pl.edu.agh.nlp.spark.algorithms;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.rdd.EmptyRDD;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.Article;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import scala.Tuple2;

public class SparkClassification implements Serializable {

	private static final long serialVersionUID = -2451802483479490942L;
	private final static double[] splitTable = { 0.6, 0.4 };

	public NaiveBayesModel builidModel(List<String> tableNames) {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		HashingTF htf = new HashingTF(10000);

		JavaRDD<LabeledPoint> training = new EmptyRDD<LabeledPoint>(sc, scala.reflect.ClassTag$.MODULE$.apply(LabeledPoint.class))
				.toJavaRDD();
		JavaRDD<LabeledPoint> test = new EmptyRDD<LabeledPoint>(sc, scala.reflect.ClassTag$.MODULE$.apply(LabeledPoint.class)).toJavaRDD();

		int i = 0;
		for (String tableName : tableNames) {
			JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(), "select tekst from " + tableName
					+ " where  ? <= id AND id <= ?", 1, 2000, 10, new ArticleMapper());
			final int j = i;
			JavaRDD<LabeledPoint> parsedData = data.map(a -> new LabeledPoint(j, htf.transform(Arrays.asList(a.getText().split(" ")))));
			JavaRDD<LabeledPoint>[] splits = parsedData.randomSplit(splitTable);
			training = training.union(splits[0]);
			test = test.union(splits[1]);
			i++;
		}

		final NaiveBayesModel model = NaiveBayes.train(training.rdd());

		JavaPairRDD<Double, Double> predictionAndLabel = test.mapToPair(p -> new Tuple2<Double, Double>(model.predict(p.features()), p
				.label()));
		long accuracy = predictionAndLabel.filter(pl -> {
			return pl._1().equals(pl._2());
		}).count();

		System.out.println("Skutecznosc: " + accuracy / (double) test.count());
		return model;
	}

	public static void test(NaiveBayesModel model) {
		HashingTF htf = new HashingTF(10000);
		String zdanie1 = "W miniony weekend Milik i Lewandowski zdobyli po dwie bramki dla swoich klubów. Ten pierwszy przyczyni³ siê do wyjazdowego zwyciêstwa Ajaksu Amsterdam z Heerenveen 4:1. Z kolei król strzelców Bundesligi zdoby³ dwa gole dla Bayernu, który w Bremie pokona³ Werder 4:0. ";
		String zdanie2 = "Dwa dni po referendum, w którym przy³¹czenie Krymu do Federacji Rosyjskiej popar³o 97 proc. g³osuj¹cych, prezydent Rosji W³adimir Putin podpisa³ stosowny traktat, przez co nale¿¹cy do Ukrainy pó³wysep i Sewastopol (jako miasto o znaczeniu federalnym) sta³y siê oficjalnie czêœciami pañstwa rosyjskiego. ";

		System.out.println(model.predict(htf.transform(Arrays.asList(zdanie1.split(" ")))));
		System.out.println(model.predict(htf.transform(Arrays.asList(zdanie2.split(" ")))));
	}

	public static void main(String[] args) {
		String[] s = { "ARTYKULY_WIADOMOSCI", "ARTYKULY_SPORT" };
		List<String> tableNames = Arrays.asList(s);

		SparkClassification sparkClassification = new SparkClassification();
		NaiveBayesModel model = sparkClassification.builidModel(tableNames);
		test(model);

	}
}