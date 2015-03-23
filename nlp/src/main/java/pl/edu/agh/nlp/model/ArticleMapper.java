package pl.edu.agh.nlp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.spark.api.java.function.Function;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Article.Category;

public class ArticleMapper implements Function<ResultSet, Article> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Article call(ResultSet resultSet) throws Exception {

		Article article = new Article();
		try {
			article.setIntro(resultSet.getString("intro"));
		} catch (SQLException e) {
		}
		try {
			article.setText(resultSet.getString("text"));
		} catch (SQLException e) {
		}
		try {
			article.setTitle(resultSet.getString("title"));
		} catch (SQLException e) {
		}
		try {
			article.setId(resultSet.getLong("id"));
		} catch (SQLException e) {
		}

		try {
			article.setCategory(Category.valueOf(resultSet.getString("category").toUpperCase()));
		} catch (SQLException e) {
		}

		return article;
	}
}
