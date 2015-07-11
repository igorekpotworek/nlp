package pl.edu.agh.nlp.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@Autowired
	private AsyncController asyncController;

	@RequestMapping(value = "/recommend/{userId}")
	public ResponseEntity<List<Rate>> getRecommendedArticles(@PathVariable Integer userId) {
		Rating[] r;
		try {
			r = collaborativeFiltering.recommend(userId);
		} catch (AbsentModelException e) {
			collaborativeFiltering.buildModel();
			try {
				r = collaborativeFiltering.recommend(userId);
			} catch (AbsentModelException e1) {
				return new ResponseEntity<List<Rate>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		List<Rate> recommendedArticles = Arrays.asList(r).stream().map(new RatingToRateConverter()).collect(Collectors.toList());
		return new ResponseEntity<List<Rate>>(recommendedArticles, HttpStatus.OK);
	}

	@RequestMapping(value = "/recommend/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildRecommenderModel();
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
