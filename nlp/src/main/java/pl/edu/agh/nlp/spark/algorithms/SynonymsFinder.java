package pl.edu.agh.nlp.spark.algorithms;

import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import pl.edu.agh.nlp.spark.utils.Tokenizer;
import scala.Tuple2;

public class SynonymsFinder {

	private final static Tokenizer tokenizer = new Tokenizer();

	public Tuple2<String, Object>[] find(String word) {
		SparkContext sparkContext = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sparkContext);
		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select tekst from ARTYKULY_WIADOMOSCI where  ? <= id AND id <= ?", 1, 10000, 10, new ArticleMapper());

		JavaRDD<List<String>> javaRdd = data.map(r -> tokenizer.tokenize(r.getText()));
		System.out.println(javaRdd.count());
		Word2Vec word2vec = new Word2Vec();
		Word2VecModel model = word2vec.fit(javaRdd);
		// Set<String> s = model.getVectors().keySet().toSet();

		return model.findSynonyms(word, 40);
	}

	public static void main(String[] args) {

		SynonymsFinder synonymsFinder = new SynonymsFinder();
		Tuple2<String, Object>[] t = synonymsFinder.find("ukraina");

		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]._1);
			System.out.println(t[i]._2);
		}

	}
}
