package pl.edu.agh.nlp.model.dao;

import pl.edu.agh.nlp.model.entities.TopicArticle;

import java.util.List;

public interface TopicsArticlesDao {

	void save(final List<TopicArticle> topics);

	List<TopicArticle> findByTopicsByArticleId(final Integer articleId);

	void deleteAll();
}
