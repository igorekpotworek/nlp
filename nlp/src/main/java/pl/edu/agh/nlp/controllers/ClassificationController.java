package pl.edu.agh.nlp.controllers;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Article.Category;
import pl.edu.agh.nlp.spark.algorithms.classification.SparkClassification;

@RestController
public class ClassificationController {

	@Autowired
	private ArticlesDao articlesDao;
	@Autowired
	private SparkClassification sparkClassification;

	@RequestMapping(value = "/classify/{articleId}")
	public Category classifyArticleById(@PathVariable Integer articleId) throws AbsentModelException {
		Article article = articlesDao.findById(articleId);
		try {
			return sparkClassification.predictCategory(article.getText());
		} catch (AbsentModelException e) {
			sparkClassification.builidModel();
			return sparkClassification.predictCategory(article.getText());
		}
	}

	@RequestMapping(value = "/classify", method = RequestMethod.POST)
	public Category classifyArticle(@RequestBody Article article) throws AbsentModelException {
		try {
			return sparkClassification.predictCategory(article.getText());
		} catch (AbsentModelException e) {
			sparkClassification.builidModel();
			return sparkClassification.predictCategory(article.getText());
		}
	}

	@Async
	@RequestMapping(value = "/classify/rebuild")
	public Future<String> rebuildModel() {
		sparkClassification.builidModel();
		return new AsyncResult<String>("ok");
	}

}
