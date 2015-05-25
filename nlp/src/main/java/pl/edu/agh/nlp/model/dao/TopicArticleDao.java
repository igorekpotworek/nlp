package pl.edu.agh.nlp.model.dao;

import java.util.List;

import pl.edu.agh.nlp.model.entities.TopicArticle;

public interface TopicArticleDao {

	public void insert(final List<TopicArticle> topics);

	public List<TopicArticle> findByTopicsByArticleId(final Long articleId);
}
