package pl.edu.agh.nlp.model.dao;

import java.util.List;

import pl.edu.agh.nlp.model.entities.TopicWord;

public interface TopicsWordsDao {

	public void insert(final List<TopicWord> topicsWords);

	public List<TopicWord> findAll();

}
