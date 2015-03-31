package pl.edu.agh.nlp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(value = "/topics/find")
	public List<TopicArticle> getTopicsOfArticle(@RequestParam(value = "article") Long article) {
		return topicArticleDao.findByTopicsByArticleId(article);
	}

	@RequestMapping(value = "/topics/all")
	public List<TopicWord> getAllTopics(@RequestParam(value = "article") Long article) {
		return topicsWordsDao.findAll();
	}
}
