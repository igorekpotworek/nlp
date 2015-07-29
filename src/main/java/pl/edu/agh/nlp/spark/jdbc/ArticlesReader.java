package pl.edu.agh.nlp.spark.jdbc;

import java.io.Serializable;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.rdd.JdbcRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.model.RatingMapper;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.SparkContextFactory;

@Service
public class ArticlesReader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7360854451347488378L;

	@Autowired
	private NlpServiceConnectionFactory nlpServiceConnectionFactory;

	public JavaRDD<Article> readArticlesToRDD() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		return JdbcRDD.create(jsc, nlpServiceConnectionFactory, "select * from articles where  ? <= id AND id <= ?", 0, 600000, 4,
				new ArticleMapper());
	}

	public JavaRDD<Rating> readArticlesHistoryToRDD() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		return JdbcRDD.create(jsc, nlpServiceConnectionFactory, "select * from rates  where  ? <= userId AND userId <= ?", 1, 6040, 4,
				new RatingMapper());
	}
}
