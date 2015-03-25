package pl.edu.agh.nlp.model.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.model.entities.TopicWord;

public class TopicsWordsDao extends NamedParameterJdbcDaoSupport {
	public void insert(List<TopicWord> topicsWords) {
		String sql = "insert into topics_articles(topicId, word, weight) values (:topicId, :word, :weight)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(topicsWords.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}

	public List<TopicWord> findAll() {
		String sql = "select topicId, word, weight from  values topics_articles";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<TopicWord>(TopicWord.class));
	}

}
