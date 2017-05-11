package pl.edu.agh.nlp.model.dao;

import pl.edu.agh.nlp.model.entities.Topic;

import java.util.List;

public interface TopicsDao {

	void save(final List<Topic> topicsWords);

	List<Topic> findAll();

	void deleteAll();

}
