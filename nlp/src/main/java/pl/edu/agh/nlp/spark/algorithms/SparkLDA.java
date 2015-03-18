package pl.edu.agh.nlp.spark.algorithms;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.Article;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;

public class SparkLDA {
	public static void main(String[] args) {
		SparkContext sparkContext = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);

		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select tekst from ARTYKULY_WIADOMOSCI where  ? <= id AND id <= ?", 1, 5000, 10, new ArticleMapper());

		HashingTF hashingTF = new HashingTF(10000);

		JavaRDD<List<String>> javaRdd = data.map(r -> Arrays.asList(r.getText().trim().split(" ")));
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
