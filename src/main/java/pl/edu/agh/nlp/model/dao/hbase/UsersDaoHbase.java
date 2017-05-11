package pl.edu.agh.nlp.model.dao.hbase;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.User;

public class UsersDaoHbase extends NamedParameterJdbcDaoSupport implements UsersDao {

	public void save(final User user) {
		String sql = "upsert into users(id, firstname, lastname) values (NEXT VALUE FOR users_seq, :firstname, :lastname)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(user));
	}
}
