package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.model.entities.Rate;
import pl.edu.agh.nlp.spark.algorithms.recommendations.CollaborativeFiltering;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecommenderController {

	@NonNull
	private final CollaborativeFiltering collaborativeFiltering;
	@NonNull
	private final AsyncController asyncController;

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
				return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
			}
		}
		List<Rate> recommendedArticles = Arrays.stream(r).map(new RatingToRateConverter()).collect(toList());
		return new ResponseEntity<>(recommendedArticles, OK);
	}

	@RequestMapping(value = "/recommend/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildRecommenderModel();
		return new ResponseEntity<>(OK);
	}
}
