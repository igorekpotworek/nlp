package pl.edu.agh.nlp.model.dao;

import java.util.List;

import pl.edu.agh.nlp.model.entities.Article;

public interface ArticlesDao {

	public void insert(final Article article);

	public List<Article> searchArticles(final String sentence);

	public Article findById(final Integer id);

	public List<Article> findAll();

	public void updateBatch(final List<Article> articles);
}
