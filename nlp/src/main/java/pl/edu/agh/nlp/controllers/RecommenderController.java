package pl.edu.agh.nlp.controllers;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.entities.TopicWord;

@RestController
public class RecommenderController {
	@RequestMapping(value = "/recommend")
	public List<TopicWord> getRelatedArticles(@RequestParam(value = "userId") Long userId) {
		return null;
	}

	@Async
	@RequestMapping(value = "/recommend/rebuild")
	public Future<String> rebuildModel() {
		return new AsyncResult<String>("ok");
	}
}
