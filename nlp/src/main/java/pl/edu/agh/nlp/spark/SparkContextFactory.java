package pl.edu.agh.nlp.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

public class SparkContextFactory {
	private static SparkContext sc;

	public static SparkContext getSparkContext() {
		if (sc == null) {
			SparkConf conf = new SparkConf().setAppName("NLP").setMaster("local[2]");
			sc = new SparkContext(conf);
		}
		return sc;
	}
}
