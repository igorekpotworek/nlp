package pl.edu.agh.nlp.spark.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.Article;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import scala.Tuple2;

public class SynonymsFinder {

	public Tuple2<String, Object>[] find(String word) {
		SparkContext sparkContext = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);
		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select tekst from ARTYKULY_WIADOMOSCI where  ? <= id AND id <= ?", 1, 5000, 10, new ArticleMapper());

		JavaRDD<List<String>> javaRdd = data.map(r -> new ArrayList<String>(Arrays.asList(r.getText().trim().split(" "))));

		Word2Vec word2vec = new Word2Vec();
		Word2VecModel model = word2vec.fit(javaRdd);

		return model.findSynonyms(word, 40);

	}

	public static void main(String[] args) {
		SynonymsFinder synonymsFinder = new SynonymsFinder();
		Tuple2<String, Object>[] t = synonymsFinder.find("tusk");
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]._1);
			System.out.println(t[i]._2);
		}
	}
}
