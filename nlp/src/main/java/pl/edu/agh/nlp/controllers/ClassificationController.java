package pl.edu.agh.nlp.controllers;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Article.Category;

@RestController
public class ClassificationController {

	@RequestMapping(value = "/classify")
	public Category classifyArticleById(@RequestParam(value = "article") Long article) {
		return null;
	}

	@RequestMapping(value = "/classify", method = RequestMethod.POST)
	public Category classifyArticleById(@RequestBody Article article) {
		return null;
	}

	@Async
	@RequestMapping(value = "/classify/rebuild")
	public Future<String> rebuildModel() {
		return new AsyncResult<String>("ok");
	}

}
