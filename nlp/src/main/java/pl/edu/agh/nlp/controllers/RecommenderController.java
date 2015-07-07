package pl.edu.agh.nlp.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.converters.RatingToRateConverter;
import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.model.entities.Rate;
import pl.edu.agh.nlp.spark.algorithms.recommendations.CollaborativeFiltering;

@RestController
public class RecommenderController {
	@Autowired
	private CollaborativeFiltering collaborativeFiltering;

	@RequestMapping(value = "/recommend/{userId}")
	public List<Rate> getRecommendedArticles(@PathVariable Integer userId) throws AbsentModelException {
		Rating[] r;
		try {
			r = collaborativeFiltering.recommend(userId);
		} catch (AbsentModelException e) {
			collaborativeFiltering.builidModel();
			r = collaborativeFiltering.recommend(userId);
		}
		return Arrays.asList(r).stream().map(new RatingToRateConverter()).collect(Collectors.toList());
	}

	@Async
	@RequestMapping(value = "/recommend/rebuild")
	public Future<String> rebuildModel() {
		collaborativeFiltering.builidModel();
		return new AsyncResult<String>("ok");
	}
}
