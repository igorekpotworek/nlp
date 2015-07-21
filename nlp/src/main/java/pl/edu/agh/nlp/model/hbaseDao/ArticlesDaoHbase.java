package pl.edu.agh.nlp.model.hbaseDao;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import pl.edu.agh.nlp.converters.HitSourceToArticleConverter;
import pl.edu.agh.nlp.elasticsearch.ElasticsearchSessionManager;
import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

public class ArticlesDaoHbase extends NamedParameterJdbcDaoSupport implements ArticlesDao {
	private final int SCROLL_SIZE = 1000;

	@Autowired
	private ElasticsearchSessionManager elasticsearchSessionManager;

	@Override
	public void insert(final Article article) {
		String sql = "upsert into articles(id, title, intro, text) values (NEXT VALUE FOR articles_seq, :title, :intro, :text)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(article));
	}

	@Override
	public List<Article> searchArticles(final String sentence) {
		List<Article> articles = new ArrayList<Article>();
		Client client = elasticsearchSessionManager.getClient();
		HitSourceToArticleConverter converter = new HitSourceToArticleConverter();
		SearchResponse response = null;
		int i = 0;
		while (response == null || response.getHits().hits().length != 0) {
			response = client.prepareSearch("articles")
					.setQuery(QueryBuilders.multiMatchQuery(sentence, "TITLE", "INTRO", "TEXT").analyzer("polish")).setSize(SCROLL_SIZE)
					.setFrom(i * SCROLL_SIZE).execute().actionGet();
			for (SearchHit hit : response.getHits())
				articles.add(converter.apply(hit.getSource()));

			i++;
		}
		return articles;
	}

	@Override
	public Article findById(final Integer id) {
		String sql = "select id, title, intro, text from articles where id=:id";
		SqlParameterSource parameters = new MapSqlParameterSource("id", id);
		return DataAccessUtils
				.singleResult(getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<Article>(Article.class)));
	}

	@Override
	public List<Article> findAll() {
		String sql = "select id, title, intro, text from articles";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<Article>(Article.class));
	}

	@Override
	public void updateBatch(final List<Article> articles) {
		String sql = "upsert into articles(id, title, intro, text) values (:id, :title, :intro, :text)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(articles.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}
}
