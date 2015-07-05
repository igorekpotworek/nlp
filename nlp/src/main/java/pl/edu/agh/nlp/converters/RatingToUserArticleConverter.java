package pl.edu.agh.nlp.converters;

import java.util.function.Function;

import org.apache.spark.mllib.recommendation.Rating;

import pl.edu.agh.nlp.model.entities.UserArticle;

public class RatingToUserArticleConverter implements Function<Rating, UserArticle> {

	@Override
	public UserArticle apply(Rating rating) {
		UserArticle userArticle = new UserArticle();
		userArticle.setArticleId(rating.product());
		userArticle.setUserId(rating.user());
		userArticle.setRating(rating.rating());
		return userArticle;
	}

}