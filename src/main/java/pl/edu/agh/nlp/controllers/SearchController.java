package pl.edu.agh.nlp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Article>> search(@RequestBody SearchBody body) {
		return new ResponseEntity<List<Article>>(articlesDao.searchArticles(body.getSentence()), HttpStatus.OK);
	}

}
