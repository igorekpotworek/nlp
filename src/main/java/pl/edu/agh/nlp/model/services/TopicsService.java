package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;

import java.util.List;

public interface TopicsService {
	void save(@NonNull List<Topic> topics, @NonNull List<TopicArticle> topicsArticles);

	List<Topic> findAll();

	List<TopicArticle> findByTopicsByArticleId(Integer articleId);
}
