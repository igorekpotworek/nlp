package pl.edu.agh.nlp.model.dao;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.entities.User;

public class UsersDao extends NamedParameterJdbcDaoSupport {

	public void insert(User user) {
		String sql = "insert into users(firstname, lastname) values (:firstname, :lastname)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(user));
	}
}
