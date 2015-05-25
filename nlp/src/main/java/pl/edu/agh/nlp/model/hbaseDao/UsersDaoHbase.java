package pl.edu.agh.nlp.model.hbaseDao;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.User;

public class UsersDaoHbase extends NamedParameterJdbcDaoSupport implements UsersDao {

	public void insert(final User user) {
		String sql = "upsert into users(firstname, lastname) values (:firstname, :lastname)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(user));
	}
}
