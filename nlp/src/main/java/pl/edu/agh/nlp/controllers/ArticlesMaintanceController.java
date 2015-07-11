package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> addArticle(@RequestBody Article article) {
		articlesDao.insert(article);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "/find/{articleId}")
	public ResponseEntity<Article> findById(@PathVariable Integer articleId) {
		return new ResponseEntity<Article>(articlesDao.findById(articleId), HttpStatus.OK);

	}
}
