package pl.edu.agh.nlp.model;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.Rating;

import java.sql.ResultSet;

import static pl.edu.agh.nlp.model.ResultSetExtension.getDoubleOrNull;
import static pl.edu.agh.nlp.model.ResultSetExtension.getIntOrNull;

public class RatingMapper implements Function<ResultSet, Rating> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String USER_ID = "userId";
	private static final String ARTICLE_ID = "articleId";
	private static final String RATING = "rating";

	@Override
	public Rating call(ResultSet resultSet) throws Exception {
		Integer userId = getIntOrNull(resultSet, USER_ID);
		Integer articleId = getIntOrNull(resultSet,ARTICLE_ID);
		Double rating = getDoubleOrNull(resultSet,RATING);
		return new Rating(userId, articleId, rating);
	}
}
