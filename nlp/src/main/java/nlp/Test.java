package nlp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class Test {
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		SparkConf sparkConf = new SparkConf().setAppName("Nlp").setMaster(
				"local[2]");

		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		Map<String, String> options = new HashMap<String, String>();
		options.put("url",
				"jdbc:postgresql://127.0.0.1:6543/postgres?user=postgres&password=postgres");
		options.put("dbtable", "public.TMP");

		DataFrame jdbcDF = sqlContext.load("jdbc", options);

		JavaRDD<Row> data = jdbcDF.javaRDD();

		HashingTF hashingTF = new HashingTF();

		JavaRDD<List<String>> javaRdd = data.map(r -> new ArrayList<String>(
				Arrays.asList(r.getString(4).trim().split(" "))));

		JavaRDD<Vector> parsedData = hashingTF.transform(javaRdd);
		JavaPairRDD<Long, Vector> corpus = JavaPairRDD.fromJavaRDD(parsedData
				.zipWithIndex().map(t -> t.swap()));

		corpus.cache();

		DistributedLDAModel ldaModel = new LDA().setK(10).run(corpus);

		System.out.println("Learned topics (as distributions over vocab of "
				+ ldaModel.vocabSize() + " words):");
		Matrix topics = ldaModel.topicsMatrix();
		for (int topic = 0; topic < 3; topic++) {
			System.out.print("Topic " + topic + ":");
			for (int word = 0; word < ldaModel.vocabSize(); word++) {
				System.out.print(" " + topics.apply(word, topic));
			}
			System.out.println();
		}
	}
}
