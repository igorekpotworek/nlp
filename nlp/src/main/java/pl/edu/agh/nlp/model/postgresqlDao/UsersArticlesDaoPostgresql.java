package pl.edu.agh.nlp.model.postgresqlDao;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.dao.UsersArticlesDao;
import pl.edu.agh.nlp.model.entities.UserArticle;

public class UsersArticlesDaoPostgresql extends NamedParameterJdbcDaoSupport implements UsersArticlesDao {
	public void insert(final UserArticle userArticle) {
		String sql = "insert into users_articles(userId, articleId, rating) values (:userId, :articleId, :rating)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(userArticle));
	}
}
