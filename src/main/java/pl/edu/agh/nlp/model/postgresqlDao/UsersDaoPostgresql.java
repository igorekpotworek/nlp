package pl.edu.agh.nlp.model.postgresqlDao;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.User;

public class UsersDaoPostgresql extends NamedParameterJdbcDaoSupport implements UsersDao {

	public void insert(final User user) {
		String sql = "insert into users(firstname, lastname) values (:firstname, :lastname)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(user));
	}
}
