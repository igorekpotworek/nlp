package pl.edu.agh.nlp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.algorithms.clustering.SimilarArticlesFinder;

@RestController
public class RelatedArticlesController {

	@Autowired
	private ArticlesDao articlesDao;
	@Autowired
	private SimilarArticlesFinder similarArticlesFinder;
	@Autowired
	private AsyncController asyncController;

	@RequestMapping(value = "/relatedArticles/{articleId}")
	public ResponseEntity<List<Article>> getRelatedArticles(@PathVariable Integer articleId) {
		Article article = articlesDao.findById(articleId);

		return null;
	}

	@RequestMapping(value = "/relatedArticles/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildSimilarArticlesFinderModel();
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
