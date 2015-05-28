package pl.edu.agh.nlp.model.hbaseDao;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

public class ArticlesDaoHbase extends NamedParameterJdbcDaoSupport implements ArticlesDao {

	public void insert(final Article article) {
		String sql = "upsert into articles(title, intro, text) values (:title, :intro, :text)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(article));
	}

	public List<Article> searchArticles(final String sentence) {
		// TODO dodac obsluge elasticsearch
		return null;
	}

	public Article findById(final Long id) {
		String sql = "select id, title, intro, text from articles where id=:id";
		SqlParameterSource parameters = new MapSqlParameterSource("id", id);
		return DataAccessUtils.singleResult(getNamedParameterJdbcTemplate().query(sql, parameters,
				new BeanPropertyRowMapper<Article>(Article.class)));
	}

	public List<Article> findAll() {
		String sql = "select id, title, intro, text from articles";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<Article>(Article.class));
	}

	public void updateBatch(final List<Article> articles) {
		String sql = "update articles set text=:text, intro=:intro where id=:id";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(articles.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}
}
