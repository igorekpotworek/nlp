package pl.edu.agh.nlp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.spark.api.java.function.Function;

public class ArticleMapper implements Function<ResultSet, Article> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Article call(ResultSet resultSet) throws Exception {

		Article article = new Article();
		try {
			article.setIntro(resultSet.getString("wstep"));
		} catch (SQLException e) {
		}
		try {
			article.setText(resultSet.getString("tekst"));
		} catch (SQLException e) {
		}
		try {
			article.setTitle(resultSet.getString("tytul"));
		} catch (SQLException e) {
		}
		try {
			article.setId(resultSet.getLong("id"));
		} catch (SQLException e) {
		}
		return article;
	}

}
