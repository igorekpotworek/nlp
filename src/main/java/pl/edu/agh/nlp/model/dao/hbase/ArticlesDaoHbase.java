package pl.edu.agh.nlp.model.dao.hbase;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import pl.edu.agh.nlp.elasticsearch.ElasticsearchSessionManager;
import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticlesDaoHbase extends NamedParameterJdbcDaoSupport implements ArticlesDao {

	private static final int SCROLL_SIZE = 1000;

	private static final String INTRO = "INTRO";
	private static final String TEXT = "TEXT";
	private static final String TITLE = "TITLE";

	private static final String ARTICLES_INDEX = "articles";

	@NonNull
	private final ElasticsearchSessionManager esSessionManager;

	@Override
	public void save(final Article article) {
		String sql = "upsert into articles(id, title, intro, text) values (NEXT VALUE FOR articles_seq, :title, :intro, :text)";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(article));
	}

	@Override
	public List<Article> findBySentence(final String sentence) {
		List<Article> articles = new ArrayList<>();
		Client client = esSessionManager.getClient();
		HitSourceToArticleConverter converter = new HitSourceToArticleConverter();
		SearchResponse response = null;
		int i = 0;
		while (response == null || response.getHits().hits().length != 0) {
			response = client.prepareSearch(ARTICLES_INDEX)
					.setQuery(multiMatchQuery(sentence, TITLE, INTRO, TEXT).analyzer("polish"))
					.setSize(SCROLL_SIZE)
					.setFrom(i * SCROLL_SIZE)
					.execute()
					.actionGet();

			for (SearchHit hit : response.getHits())
				articles.add(converter.apply(hit.getSource()));

			i++;
		}
		return articles;
	}

	@Override
	public Article findById(final Integer id) {
		String sql = "select id, title, intro, text from articles where id=:id";
		SqlParameterSource params = new MapSqlParameterSource("id", id);
		return singleResult(getNamedParameterJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<>(Article.class)));
	}

	@Override
	public List<Article> findAll() {
		String sql = "select id, title, intro, text from articles";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(Article.class));
	}

	@Override
	public void save(final List<Article> articles) {
		String sql = "upsert into articles(id, title, intro, text) values (:id, :title, :intro, :text)";
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(articles.toArray());
		getNamedParameterJdbcTemplate().batchUpdate(sql, params);
	}
}
