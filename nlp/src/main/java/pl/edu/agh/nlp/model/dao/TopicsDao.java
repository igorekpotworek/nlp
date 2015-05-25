package pl.edu.agh.nlp.model.dao;

import java.util.List;

import pl.edu.agh.nlp.model.entities.Topic;

public interface TopicsDao {

	public void insert(final List<Topic> topics);
}
