package pl.edu.agh.nlp.model.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.model.entities.Topic;

public class TopicsDao extends NamedParameterJdbcDaoSupport {

	public void insert(List<Topic> topics) {
		String sql = "insert into topics(id) values (:id)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(topics.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}

}
