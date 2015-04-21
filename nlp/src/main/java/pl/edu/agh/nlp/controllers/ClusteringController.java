package pl.edu.agh.nlp.controllers;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.TopicArticleDao;
import pl.edu.agh.nlp.model.dao.TopicsWordsDao;
import pl.edu.agh.nlp.model.entities.TopicArticle;
import pl.edu.agh.nlp.model.entities.TopicWord;

@RestController
public class ClusteringController {
	@Autowired
	private TopicArticleDao topicArticleDao;

	@Autowired
	private TopicsWordsDao topicsWordsDao;

	@RequestMapping(value = "/topics/article")
	public List<TopicArticle> getTopicsOfArticle(@RequestParam(value = "articleId") Long articleId) {
		return topicArticleDao.findByTopicsByArticleId(articleId);
	}

	@RequestMapping(value = "/topics")
	public List<TopicWord> getAllTopics() {
		return topicsWordsDao.findAll();
	}

	@Async
	@RequestMapping(value = "/topics/rebuild")
	public Future<String> rebuildModel() {
		return new AsyncResult<String>("ok");
	}

}
