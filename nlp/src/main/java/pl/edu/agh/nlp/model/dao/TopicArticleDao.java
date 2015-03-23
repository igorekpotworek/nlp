package pl.edu.agh.nlp.model.dao;

import java.util.List;

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

}
