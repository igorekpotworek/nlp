package pl.edu.agh.nlp.spark.jdbc;

import com.clearspring.analytics.util.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

import java.io.Serializable;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticlesReader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7360854451347488378L;

	@NonNull
	private final NlpServiceConnectionFactory nlpServiceConnectionFactory;

	public JavaRDD<Article> readArticlesToRDD() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		final String query = "select * from articles where ? <= id AND id <= ?";
		return JdbcRDD.create(jsc, nlpServiceConnectionFactory, query, 0, 600000, 4, new ArticleMapper());
	}

	public JavaRDD<Rating> readArticlesHistoryToRDD() {
		SparkContext sc = SparkContextFactory.getSparkContext();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		final String query = "select * from rates where ? <= userId AND userId <= ?";
		return JdbcRDD.create(jsc, nlpServiceConnectionFactory, query, 1, 6040, 4, new RatingMapper());
	}

	public JavaRDD<Article> loadAndPrepareData() {
		JavaRDD<Article> data = readArticlesToRDD();
		data = data.filter(a -> StringUtils.isNotBlank(a.getText()))
				.filter(a -> a.getCategory() != null);
		final Long classSize = data.keyBy(Article::getCategory)
				.countByKey()
				.values()
				.stream()
				.mapToLong(p -> (long) p).min().getAsLong();
		return data.groupBy(Article::getCategory)
				.map(t -> Lists.newArrayList(t._2).subList(0, classSize.intValue()))
				.flatMap(f -> f);
	}
}
