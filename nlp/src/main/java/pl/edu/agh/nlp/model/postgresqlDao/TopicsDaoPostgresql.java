package pl.edu.agh.nlp.model.postgresqlDao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Topic;

public class TopicsDaoPostgresql extends NamedParameterJdbcDaoSupport implements TopicsDao {
	public void insert(final List<Topic> topicsWords) {
		String sql = "insert into topics(topicId, word, weight) values (:topicId, :word, :weight)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(topicsWords.toArray());

		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}

	public List<Topic> findAll() {
		String sql = "select topicId, word, weight from topics";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<Topic>(Topic.class));
	}

	public void deleteAll() {
		String sql = "delete from topics";
		getJdbcTemplate().update(sql);
	}

}
