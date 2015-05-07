package pl.edu.agh.nlp.controllers;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/classify")
	public Category classifyArticleById(@RequestParam(value = "articleId") Long articleId) throws AbsentModelException {
		Article article = articlesDao.findById(articleId);
		SparkClassification sparkClassification = SparkClassification.getSparkClassification();
		try {
			return sparkClassification.predictCategory(article.getText());
		} catch (AbsentModelException e) {
			sparkClassification.builidModel();
			return sparkClassification.predictCategory(article.getText());
		}
	}

	@RequestMapping(value = "/classify", method = RequestMethod.POST)
	public Category classifyArticle(@RequestBody Article article) throws AbsentModelException {
		SparkClassification sparkClassification = SparkClassification.getSparkClassification();
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
		SparkClassification sparkClassification = SparkClassification.getSparkClassification();
		sparkClassification.builidModel();
		return new AsyncResult<String>("ok");
	}

}
