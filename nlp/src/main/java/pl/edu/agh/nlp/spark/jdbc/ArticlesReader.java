package pl.edu.agh.nlp.spark.jdbc;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.model.RatingMapper;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.SparkContextFactory;

public class ArticlesReader {

	public static JavaRDD<Article> readArticlesToRDD() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		return JdbcRDD.create(jsc, new PostgresConnection(), "select * from articles where  ? <= id AND id <= ?", 0, 600000, 4,
				new ArticleMapper());
	}

	public static JavaRDD<Rating> readArticlesHistoryToRDD() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		return JdbcRDD.create(jsc, new PostgresConnection(), "select * from users_articles where  ? <= userId AND userId <= ?", 1, 1000, 2,
				new RatingMapper());
	}
}
