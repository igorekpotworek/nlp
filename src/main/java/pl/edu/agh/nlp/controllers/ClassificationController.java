package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@Autowired
	private AsyncController asyncController;

	@RequestMapping(value = "/classify/{articleId}")
	public ResponseEntity<Category> classifyArticleById(@PathVariable Integer articleId) {
		Article article = articlesDao.findById(articleId);
		if (article != null) {
			try {
				return new ResponseEntity<Category>(sparkClassification.predictCategory(article.getText()), HttpStatus.OK);
			} catch (AbsentModelException e) {
				sparkClassification.buildModel();
				try {
					return new ResponseEntity<Category>(sparkClassification.predictCategory(article.getText()), HttpStatus.OK);
				} catch (AbsentModelException e1) {
					return new ResponseEntity<Category>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} else
			return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/classify", method = RequestMethod.POST)
	public ResponseEntity<Category> classifyArticle(@RequestBody Article article) {
		try {
			return new ResponseEntity<Category>(sparkClassification.predictCategory(article.getText()), HttpStatus.OK);
		} catch (AbsentModelException e) {
			sparkClassification.buildModel();
			try {
				return new ResponseEntity<Category>(sparkClassification.predictCategory(article.getText()), HttpStatus.OK);
			} catch (AbsentModelException e1) {
				return new ResponseEntity<Category>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@RequestMapping(value = "/classify/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildClassificationModelAsync();
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
