package pl.edu.agh.nlp.controllers;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.utils.DataCleaner;

@RestController
public class HomeController {

	@Autowired
	private DataCleaner dataCleaner;

	@Autowired
	private ArticlesDao articlesDao;

	@Async
	@RequestMapping(value = "/clean")
	public Future<String> clean() {
		dataCleaner.cleanArticles();
		return new AsyncResult<String>("ok");
	}

}
