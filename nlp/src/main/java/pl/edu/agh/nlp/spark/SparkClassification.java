package pl.edu.agh.nlp.spark;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;

public class SparkClassification {
	public static void test() {
		SparkConf conf = new SparkConf().setAppName("Sentiment").setMaster(
				"local[2]");

		HashingTF htf = new HashingTF(10000);

		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		// Sport - label 0
		DataFrame jdbcSport = new JdbcLoader().getTableFromJdbc(sqlContext,
				"SPORT_NEW");
		JavaRDD<LabeledPoint> sportData = jdbcSport.javaRDD().map(
				row -> new LabeledPoint(0, htf.transform(Arrays.asList(row
						.getString(0).split(" ")))));
		System.out.println(sportData.count());

		// Political - label 1
		DataFrame jdbcPolitical = new JdbcLoader().getTableFromJdbc(sqlContext,
				"WIADOMOSCI_NEW");
		JavaRDD<LabeledPoint> politicalData = jdbcPolitical.javaRDD().map(
				row -> new LabeledPoint(1, htf.transform(Arrays.asList(row
						.getString(0).split(" ")))));
		System.out.println(politicalData.count());

		double[] splitTable = { 0.6, 0.4 };
		JavaRDD<LabeledPoint>[] sportSplits = sportData.randomSplit(splitTable);
		JavaRDD<LabeledPoint>[] politicalSplits = politicalData
				.randomSplit(splitTable);

		JavaRDD<LabeledPoint> training = sportSplits[0]
				.union(politicalSplits[0]);
		JavaRDD<LabeledPoint> test = sportSplits[1].union(politicalSplits[1]);

		final NaiveBayesModel model = NaiveBayes.train(training.rdd());

		JavaPairRDD<Double, Double> predictionAndLabel = test
				.mapToPair(p -> new Tuple2<Double, Double>(model.predict(p
						.features()), p.label()));
		long accuracy = predictionAndLabel.filter(pl -> {

			return pl._1().equals(pl._2());
		}).count();

		System.out.println("Skutecznosc: " + accuracy / (double) test.count());
		test1(model);
		sc.close();
	}

	public static void test1(NaiveBayesModel model) {
		HashingTF htf = new HashingTF(10000);
		String zdanie1 = "W miniony weekend Milik i Lewandowski zdobyli po dwie bramki dla swoich klubów. Ten pierwszy przyczyni³ siê do wyjazdowego zwyciêstwa Ajaksu Amsterdam z Heerenveen 4:1. Z kolei król strzelców Bundesligi zdoby³ dwa gole dla Bayernu, który w Bremie pokona³ Werder 4:0. ";
		String zdanie2 = "Dwa dni po referendum, w którym przy³¹czenie Krymu do Federacji Rosyjskiej popar³o 97 proc. g³osuj¹cych, prezydent Rosji W³adimir Putin podpisa³ stosowny traktat, przez co nale¿¹cy do Ukrainy pó³wysep i Sewastopol (jako miasto o znaczeniu federalnym) sta³y siê oficjalnie czêœciami pañstwa rosyjskiego. ";
		System.out.println(model.predict(htf.transform(Arrays.asList(zdanie1
				.split(" ")))));
		System.out.println(model.predict(htf.transform(Arrays.asList(zdanie2
				.split(" ")))));
	}

	public static void main(String[] args) {
		test();
	}
}