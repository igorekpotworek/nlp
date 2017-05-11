package pl.edu.agh.nlp.model.dao.hbase;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Article.Category;

import java.util.Map;
import java.util.function.Function;

public class HitSourceToArticleConverter implements Function<Map<String, Object>, Article> {

	private final static String ID_FIELD = "ID";
	private final static String TITLE_FIELD = "TITLE";
	private final static String INTRO_FIELD = "INTRO";
	private final static String CATEGORY_FIELD = "CATEGORY";
	private final static String TEXT_FIELD = "TEXT";

	@Override
	public Article apply(Map<String, Object> hitSource) {
		return Article.builder()
				.id((Integer) hitSource.get(ID_FIELD))
				.title((String) hitSource.get(TITLE_FIELD))
				.intro((String) hitSource.get(INTRO_FIELD))
				.text((String) hitSource.get(TEXT_FIELD))
				.category(Category.valueOf((String) hitSource.get(CATEGORY_FIELD)))
				.build();
	}

}