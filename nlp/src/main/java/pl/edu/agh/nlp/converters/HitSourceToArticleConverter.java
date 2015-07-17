package pl.edu.agh.nlp.converters;

import java.util.Map;
import java.util.function.Function;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Article.Category;

public class HitSourceToArticleConverter implements Function<Map<String, Object>, Article> {

	private final static String ID_FIELD = "TITLE";
	private final static String TITLE_FIELD = "TITLE";
	private final static String INTRO_FIELD = "TITLE";
	private final static String CATEGORY_FIELD = "TITLE";
	private final static String TEXT_FIELD = "TITLE";

	@Override
	public Article apply(Map<String, Object> hitSource) {
		Article article = new Article();
		article.setId((Integer) hitSource.get(ID_FIELD));
		article.setTitle((String) hitSource.get(TITLE_FIELD));
		article.setIntro((String) hitSource.get(INTRO_FIELD));
		article.setText((String) hitSource.get(TEXT_FIELD));
		article.setCategory(Category.valueOf((String) hitSource.get(CATEGORY_FIELD)));
		return article;
	}

}