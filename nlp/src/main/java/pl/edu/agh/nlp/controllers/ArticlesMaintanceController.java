package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

@RestController
public class ArticlesMaintanceController {

	@Autowired
	private ArticlesDao articlesDao;

	@RequestMapping(value = "/add/article", method = RequestMethod.POST)
	public void addArticle(@RequestBody Article article) {
		articlesDao.insert(article);
	}

	@RequestMapping(value = "/find/{articleId}")
	public Article findById(@PathVariable Integer articleId) {
		return articlesDao.findById(articleId);
	}

}
