package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.model.dao.TopicsArticlesDao;
import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class TopicsServiceImpl implements TopicsService {

	@NonNull
	private final TopicsDao topicsDao;
	@NonNull
	private final TopicsArticlesDao topicsArticlesDao;

	@Override
	public void save(@NonNull final List<Topic> topics, @NonNull final List<TopicArticle> topicsArticles) {
		topicsDao.deleteAll();
		topicsDao.save(topics);
		topicsArticlesDao.deleteAll();
		topicsArticlesDao.save(topicsArticles);
	}

	@Override
	public List<Topic> findAll() {
		return topicsDao.findAll();
	}

	@Override
	public List<TopicArticle> findByTopicsByArticleId(final Integer articleId) {
		return topicsArticlesDao.findByTopicsByArticleId(articleId);
	}

}
