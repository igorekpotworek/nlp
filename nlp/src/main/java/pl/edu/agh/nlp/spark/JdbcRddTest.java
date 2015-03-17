package pl.edu.agh.nlp.spark;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.Article;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;

public class JdbcRddTest {
	public void test() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select tekst from ARTYKULY_WIADOMOSCI where  ? <= id AND id <= ?", 1, 5000, 10, new ArticleMapper());

		// basic tokenization

		JavaRDD<String> parsedData = data.flatMap(a -> Arrays.asList(a.getText().split("\\W+")).stream().map(t -> t.toLowerCase())
				.collect(Collectors.toList()));

		System.out.println(parsedData.distinct().count());
		System.out.println(parsedData.sample(true, 0.3, 42).take(100));
	}
}
