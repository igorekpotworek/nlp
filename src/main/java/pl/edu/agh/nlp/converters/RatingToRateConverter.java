package pl.edu.agh.nlp.converters;

import java.util.function.Function;

import org.apache.spark.mllib.recommendation.Rating;

import pl.edu.agh.nlp.model.entities.Rate;

public class RatingToRateConverter implements Function<Rating, Rate> {

	@Override
	public Rate apply(Rating rating) {
		Rate rate = new Rate();
		rate.setArticleId(rating.product());
		rate.setUserId(rating.user());
		rate.setRating(rating.rating());
		return rate;
	}

}