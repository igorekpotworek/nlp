package pl.edu.agh.nlp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.Rating;

public class RatingMapper implements Function<ResultSet, Rating> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Rating call(ResultSet resultSet) throws Exception {
		Integer userId = null;
		Integer articleId = null;
		Double rating = null;
		try {
			userId = resultSet.getInt("userId");
		} catch (SQLException e) {
		}
		try {
			articleId = resultSet.getInt("articleId");
		} catch (SQLException e) {
		}
		try {
			rating = resultSet.getDouble("rating");
		} catch (SQLException e) {
		}

		return new Rating(userId.intValue(), articleId.intValue(), rating);
	}
}
