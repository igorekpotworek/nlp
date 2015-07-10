package pl.edu.agh.nlp.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.TopicsArticlesDao;
import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;
import pl.edu.agh.nlp.spark.algorithms.lda.SparkLDA;

@RestController
public class ClusteringController {
	@Autowired
	private TopicsArticlesDao topicsArticlesDao;

	@Autowired
	private TopicsDao topicsDao;

	@Autowired
	private SparkLDA sparkLDA;

	@RequestMapping(value = "/topics/article/{articleId}")
	public List<TopicArticle> getTopicsOfArticle(@PathVariable Integer articleId) {
		return topicsArticlesDao.findByTopicsByArticleId(articleId);
	}

	@RequestMapping(value = "/topics")
	public List<Topic> getAllTopics() {
		return topicsDao.findAll();
	}

	@RequestMapping(value = "/topics/rebuild")
	public String rebuildModel() throws IOException {
		sparkLDA.buildModelAsync();
		return "ok";
	}

}
