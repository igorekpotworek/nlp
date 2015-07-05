package pl.edu.agh.nlp.controllers;

import java.util.concurrent.Future;

import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.spark.algorithms.recommendations.CollaborativeFiltering;

@RestController
public class RecommenderController {
	@Autowired
	private CollaborativeFiltering collaborativeFiltering;

	@RequestMapping(value = "/recommend")
	public Rating[] getRelatedArticles(@RequestParam(value = "userId") Integer userId) {
		return collaborativeFiltering.recommend(userId);
	}

	@Async
	@RequestMapping(value = "/recommend/rebuild")
	public Future<String> rebuildModel() {
		collaborativeFiltering.builidModel();
		return new AsyncResult<String>("ok");
	}
}
