package pl.edu.agh.nlp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.TopicsArticlesDao;
import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;

@RestController
public class ClusteringController {
	@Autowired
	private TopicsArticlesDao topicsArticlesDao;
	@Autowired
	private TopicsDao topicsDao;
	@Autowired
	private AsyncController asyncController;

	@RequestMapping(value = "/topics/article/{articleId}")
	public ResponseEntity<List<TopicArticle>> getTopicsOfArticle(@PathVariable Integer articleId) {
		return new ResponseEntity<List<TopicArticle>>(topicsArticlesDao.findByTopicsByArticleId(articleId), HttpStatus.OK);
	}

	@RequestMapping(value = "/topics")
	public ResponseEntity<List<Topic>> getAllTopics() {
		return new ResponseEntity<List<Topic>>(topicsDao.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/topics/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildLDAModelAsync();
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

}
