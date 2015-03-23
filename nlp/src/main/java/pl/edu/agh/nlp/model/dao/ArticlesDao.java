package pl.edu.agh.nlp.model.dao;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import pl.edu.agh.nlp.model.entities.Article;

public class ArticlesDao extends NamedParameterJdbcDaoSupport {

	public void insert(Article article) {
		String sql = "insert into articles(title, intro, text) values (:title, :intro, :text)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(article));
	}

	public List<Article> searchArticles(String sentence) {
		String sql = "select id, title, intro, text from articles where vector @@ plainto_tsquery('public.polish',:sentence)";
		SqlParameterSource parameters = new MapSqlParameterSource("sentence", sentence);
		return getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<Article>(Article.class));
	}

	public Article findById(Long id) {
		String sql = "select id, title, intro, text from articles where id=:id";
		SqlParameterSource parameters = new MapSqlParameterSource("id", id);
		return DataAccessUtils.singleResult(getNamedParameterJdbcTemplate().query(sql, parameters,
				new BeanPropertyRowMapper<Article>(Article.class)));
	}
}
