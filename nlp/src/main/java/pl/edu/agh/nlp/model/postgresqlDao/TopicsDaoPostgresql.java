package pl.edu.agh.nlp.model.postgresqlDao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Topic;

public class TopicsDaoPostgresql extends NamedParameterJdbcDaoSupport implements TopicsDao {

	public void insert(final List<Topic> topics) {
		String sql = "insert into topics(id) values (:id)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(topics.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}

}
