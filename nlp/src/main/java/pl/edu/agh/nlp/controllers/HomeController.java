package pl.edu.agh.nlp.controllers;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.utils.DataCleaner;

@RestController
public class HomeController {

	@Autowired
	private DataCleaner dataCleaner;

	@Autowired
	private ArticlesDao articlesDao;

	@RequestMapping(value = "/")
	public String home() {
		return "Hello in NLP System";
	}

	@Async
	@RequestMapping(value = "/clean")
	public Future<String> clean() {
		dataCleaner.cleanArticles();
		return new AsyncResult<String>("ok");
	}

	@RequestMapping(value = "/test")
	public Article test(@RequestParam(value = "articleId") Long id) {
		Article a = articlesDao.findById(id);
		a.setText(DataCleaner.clean(articlesDao.findById(id).getText()));
		return a;
	}
}
