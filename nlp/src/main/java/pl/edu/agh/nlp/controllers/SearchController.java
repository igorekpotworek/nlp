package pl.edu.agh.nlp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

@RestController
public class SearchController {

	@Autowired
	private ArticlesDao articlesDao;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public List<Article> search(@RequestBody String sentence) {
		return articlesDao.searchArticles(sentence);
	}

}
