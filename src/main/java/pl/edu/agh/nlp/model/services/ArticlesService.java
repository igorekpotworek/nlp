package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import pl.edu.agh.nlp.exceptions.NotFoundException;
import pl.edu.agh.nlp.model.entities.Article;

import java.util.List;

public interface ArticlesService {
	void save(@NonNull Article article);

	void save(@NonNull List<Article> articles);

	List<Article> findBySentence(@NonNull String sentence);

	Article findById(@NonNull Integer id);

	Article findByIdOrThrow(@NonNull Integer id) throws NotFoundException;

	List<Article> findAll();
}
