package pl.edu.agh.nlp.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

public class SparkContextFactory {
	private static SparkContext sc;

	public static SparkContext getSparkContext() {
		if (sc == null) {
			SparkConf conf = new SparkConf().setAppName("NLP").setMaster("local[1]");
			sc = new SparkContext(conf);
		}
		return sc;
	}

	public static JavaSparkContext getJavaSparkContext() {
		return new JavaSparkContext(getSparkContext());
	}

	public static SQLContext getSQLSparkContext() {
		return new SQLContext(getSparkContext());
	}
}
