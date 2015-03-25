package pl.edu.agh.nlp.model.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.model.entities.TopicArticle;

public class TopicArticleDao extends NamedParameterJdbcDaoSupport {
	public void insert(List<TopicArticle> topics) {
		String sql = "insert into topics_articles(articleId, topicId, weight) values (:articleId, :topicId, :weight)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(topics.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}

	public List<TopicArticle> findById(Long articleId) {
		String sql = "select topicId, articleId, weight from  values topics_articles where articleId=:articleId";
		Map<String, Object> parameters = Collections.singletonMap("topicId", articleId);
		return getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<TopicArticle>(TopicArticle.class));
	}
}
