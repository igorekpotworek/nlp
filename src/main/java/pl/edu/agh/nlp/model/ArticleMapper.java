package pl.edu.agh.nlp.model;

import org.apache.spark.api.java.function.Function;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Article.Category;

import java.sql.ResultSet;
import java.util.Optional;

import static pl.edu.agh.nlp.model.ResultSetExtension.getIntOrNull;
import static pl.edu.agh.nlp.model.ResultSetExtension.getStringOrNull;

public class ArticleMapper implements Function<ResultSet, Article> {

	private static final String INTRO = "intro";
	private static final String TEXT = "text";
	private static final String TITLE = "title";
	private static final String ID = "id";
	private static final String CATEGORY = "category";

	@Override
	public Article call(ResultSet resultSet) throws Exception {
		Category category = Optional.ofNullable(getStringOrNull(resultSet,CATEGORY))
				.map(String::toUpperCase)
				.map(Category::valueOf)
				.orElse(null);

		return Article.builder()
				.intro(getStringOrNull(resultSet, INTRO))
				.text(getStringOrNull(resultSet, TEXT))
				.title(getStringOrNull(resultSet,TITLE))
				.id(getIntOrNull(resultSet,ID))
				.category(category)
				.build();
	}


}
