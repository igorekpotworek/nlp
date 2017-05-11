package pl.edu.agh.nlp.controllers;

import org.apache.spark.mllib.recommendation.Rating;
import pl.edu.agh.nlp.model.entities.Rate;

import java.util.function.Function;

public class RatingToRateConverter implements Function<Rating, Rate> {

	@Override
	public Rate apply(Rating rating) {
		return Rate.builder()
				.articleId(rating.product())
				.userId(rating.user())
				.rating(rating.rating())
				.build();
	}

}