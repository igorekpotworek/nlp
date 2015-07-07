package pl.edu.agh.nlp.controllers;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
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

	@RequestMapping(value = "/relatedArticles/{articleId}")
	public List<Article> getRelatedArticles(@PathVariable Integer articleId) {
		Article article = articlesDao.findById(articleId);

		return null;
	}

	@Async
	@RequestMapping(value = "/relatedArticles/rebuild")
	public Future<String> rebuildModel() {
		similarArticlesFinder.builidModel();
		return new AsyncResult<String>("ok");
	}
}
