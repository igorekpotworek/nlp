package pl.edu.agh.nlp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

@RestController
public class SearchController {

	@Autowired
	private ArticlesDao articlesDao;

	@RequestMapping(value = "/search")
	public List<Article> search(@RequestParam(value = "query") String sentence) {
		return articlesDao.searchArticles(sentence);
	}

	@RequestMapping(value = "/find")
	public Article findById(@RequestParam(value = "articleId") Long id) {
		return articlesDao.findById(id);
	}
}
