package pl.edu.agh.nlp.model.dao.postgresql;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.dao.RatesDao;
import pl.edu.agh.nlp.model.entities.Rate;

public class RatesDaoPostgresql extends NamedParameterJdbcDaoSupport implements RatesDao {
	public void save(final Rate userArticle) {
		String sql = "insert into rates(userId, articleId, rating) values (:userId, :articleId, :rating)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(userArticle));
	}
}
