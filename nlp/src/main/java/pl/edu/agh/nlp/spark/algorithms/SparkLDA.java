package pl.edu.agh.nlp.spark.algorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import pl.edu.agh.nlp.spark.SparkContextFactory;

public class SparkLDA {
	public static void main(String[] args) {
		SparkContext sparkContext = SparkContextFactory.getSparkContext();
		JavaSparkContext sc = new JavaSparkContext(sparkContext);
		SQLContext sqlContext = new SQLContext(sc);

		Map<String, String> options = new HashMap<String, String>();
		options.put("url", "jdbc:postgresql://127.0.0.1:6543/postgres?user=postgres&password=postgres");
		options.put("dbtable", "public.TMP");
		DataFrame jdbcDF = sqlContext.load("jdbc", options);

		HashingTF hashingTF = new HashingTF(10000);

		JavaRDD<List<String>> javaRdd = jdbcDF.javaRDD().map(r -> Arrays.asList(r.getString(4).trim().split(" ")));
		JavaRDD<Vector> parsedData = hashingTF.transform(javaRdd);
		JavaPairRDD<Long, Vector> corpus = JavaPairRDD.fromJavaRDD(parsedData.zipWithIndex().map(t -> t.swap()));

		corpus.cache();

		DistributedLDAModel ldaModel = new LDA().setK(3).run(corpus);

		System.out.println("Learned topics (as distributions over vocab of " + ldaModel.vocabSize() + " words):");
		Matrix topics = ldaModel.topicsMatrix();
		for (int topic = 0; topic < 3; topic++) {
			System.out.println("Topic " + topic + ":");
			for (int word = 0; word < ldaModel.vocabSize(); word++) {
				System.out.print(" " + topics.apply(word, topic));
			}
			System.out.println();
		}
	}
}
