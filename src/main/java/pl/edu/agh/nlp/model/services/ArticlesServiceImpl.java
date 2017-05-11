package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.exceptions.NotFoundException;
import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ArticlesServiceImpl implements ArticlesService {

	@NonNull
	private final ArticlesDao articlesDao;

	@Override
	public void save(@NonNull final Article article) {
		articlesDao.save(article);
	}

	@Override
	public void save(@NonNull final List<Article> articles) {
		articlesDao.save(articles);
	}

	@Override
	public List<Article> findBySentence(@NonNull final String sentence) {
		return articlesDao.findBySentence(sentence);
	}

	@Override
	public Article findById(@NonNull final Integer id) {
		return articlesDao.findById(id);
	}

	@Override
	public Article findByIdOrThrow(@NonNull Integer id) throws NotFoundException {
		Article article = findById(id);
		return Optional.ofNullable(article).orElseThrow(NotFoundException::new);
	}

	@Override
	public List<Article> findAll() {
		return articlesDao.findAll();
	}

}
