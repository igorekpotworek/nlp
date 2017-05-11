package pl.edu.agh.nlp.model.dao;

import pl.edu.agh.nlp.model.entities.Article;

import java.util.List;

public interface ArticlesDao {

	void save(final Article article);

	void save(final List<Article> articles);

	List<Article> findBySentence(final String sentence);

	Article findById(final Integer id);

	List<Article> findAll();

}
