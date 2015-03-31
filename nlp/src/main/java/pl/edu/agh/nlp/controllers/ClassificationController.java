package pl.edu.agh.nlp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.entities.Article.Category;

@RestController
public class ClassificationController {
	@RequestMapping(value = "/classify")
	public Category classifyArticleById(@RequestParam(value = "article") Long article) {
		return null;
	}

}
