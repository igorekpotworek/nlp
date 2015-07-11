package pl.edu.agh.nlp.controllers;

import java.util.List;

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
import pl.edu.agh.nlp.ner.PersonDetector;

@RestController
public class PersonDetectionController {
	@Autowired
	private ArticlesDao articlesDao;
	@Autowired
	private PersonDetector personDetector;
	@Autowired
	private AsyncController asyncController;

	@RequestMapping(value = "/ner/{articleId}")
	public ResponseEntity<List<String>> findPersons(@PathVariable Integer articleId) {
		try {
			Article article = articlesDao.findById(articleId);
			if (article != null)
				return new ResponseEntity<List<String>>(personDetector.detect(article.getText()), HttpStatus.OK);
			else
				return new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND);
		} catch (AbsentModelException e) {
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/ner", method = RequestMethod.POST)
	public ResponseEntity<List<String>> findPersons(@RequestBody Article article) {
		try {
			return new ResponseEntity<List<String>>(personDetector.detect(article.getText()), HttpStatus.OK);
		} catch (AbsentModelException e) {
			return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/ner/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildNERModelAsync();
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

}
