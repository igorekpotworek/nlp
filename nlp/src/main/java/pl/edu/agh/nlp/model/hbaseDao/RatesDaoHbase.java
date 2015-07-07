package pl.edu.agh.nlp.model.hbaseDao;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.dao.RatesDao;
import pl.edu.agh.nlp.model.entities.Rate;

public class RatesDaoHbase extends NamedParameterJdbcDaoSupport implements RatesDao {
	public void insert(final Rate userArticle) {
		String sql = "upsert into rates(userId, articleId, rating) values (:userId, :articleId, :rating)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(userArticle));
	}

}
