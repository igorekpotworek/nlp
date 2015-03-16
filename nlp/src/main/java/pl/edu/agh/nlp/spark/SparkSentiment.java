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

import scala.Tuple2;

/**
 * Hello world!
 *
 */
public class SparkSentiment {
	public static void test() {
		SparkConf conf = new SparkConf().setAppName("Sentiment").setMaster("local[2]");

		JavaSparkContext sc = new JavaSparkContext(conf);
		HashingTF htf = new HashingTF(10000);
		long time = System.currentTimeMillis();

		JavaRDD<LabeledPoint> positiveData = sc.textFile("hdfs://localhost:19000/data.csv").filter(text -> text.startsWith("1"))
				.map(text -> new LabeledPoint(1, htf.transform(Arrays.asList(text.substring(2).split(" ")))));
		JavaRDD<LabeledPoint> negativeData = sc.textFile("hdfs://localhost:19000/data.csv").filter(text -> text.startsWith("0"))
				.map(text -> new LabeledPoint(0, htf.transform(Arrays.asList(text.substring(2).split(" ")))));
		double[] splitTable = { 0.6, 0.4 };
		JavaRDD<LabeledPoint>[] posSplits = positiveData.randomSplit(splitTable);
		JavaRDD<LabeledPoint>[] negSplits = negativeData.randomSplit(splitTable);

		JavaRDD<LabeledPoint> training = posSplits[0].union(negSplits[0]);
		JavaRDD<LabeledPoint> test = posSplits[1].union(negSplits[1]);

		final NaiveBayesModel model = NaiveBayes.train(training.rdd());

		JavaPairRDD<Double, Double> predictionAndLabel = test.mapToPair(p -> new Tuple2<Double, Double>(model.predict(p.features()), p
				.label()));
		long accuracy = predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count();

		System.out.println("Super Zajebisty wynik : " + accuracy / (double) test.count() + "sex");
		System.out.println("Czas " + (System.currentTimeMillis() - time) / 1000);
		sc.close();
	}

	public static void main(String[] args) {
		test();
	}
}