package pl.edu.agh.nlp.spark.algorithms.lda;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import pl.edu.agh.nlp.model.dao.TopicsArticlesDao;
import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import pl.edu.agh.nlp.utils.Tokenizer;
import scala.Tuple2;

@Service
public class SparkLDA implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2830677796836853217L;

	private static final Logger logger = Logger.getLogger(SparkLDA.class);
	private static final HashingTF hashingTF = new HashingTF(1000000);

	@Autowired
	private Tokenizer tokenizer;
	@Autowired
	private TopicsDescriptionWriter topicsDescriptionWriter;
	@Autowired
	private TopicsDistributionWriter topicsDistributionWriter;

	@Autowired
	private TopicsDao topicsDao;
	@Autowired
	private TopicsArticlesDao topicsArticlesDao;

	@Autowired
	private ArticlesReader articlesReader;

	public void buildModel() {
		long time1 = System.currentTimeMillis();
		JavaPairRDD<Long, List<String>> data = loadAndPrepareData();
		long time2 = System.currentTimeMillis();
		long buildTime = (time2 - time1);
		logger.info("Time of loading and preparing data: " + buildTime + "ms");

		time1 = System.currentTimeMillis();
		Multimap<Integer, String> mapping = createMapping(data);
		time2 = System.currentTimeMillis();
		buildTime = (time2 - time1);
		logger.info("Time of creating mapping: " + buildTime + "ms");

		time1 = System.currentTimeMillis();
		JavaPairRDD<Long, Vector> corpus = bulidCorpus(data);
		corpus.cache();

		time2 = System.currentTimeMillis();
		buildTime = (time2 - time1);
		logger.info("Time of building TFIDF model: " + buildTime + "ms");

		time1 = System.currentTimeMillis();
		// Budowa modelu
		DistributedLDAModel ldaModel = new LDA().setK(100).setAlpha(1.01).run(corpus);
		time2 = System.currentTimeMillis();
		buildTime = (time2 - time1);
		logger.info("Time of building LDA model: " + buildTime + "ms");

		saveResults(getTopics(ldaModel, mapping), getTopicsArticles(ldaModel));
		logger.info("Model ready");

	}

	private JavaPairRDD<Long, Vector> bulidCorpus(JavaPairRDD<Long, List<String>> data) {
		// Budowa modelu TF
		JavaPairRDD<Long, Vector> tfidfData = data.mapValues(f -> hashingTF.transform(f));
		// Budowa modelu IDF, minimalna ilość wystapien - 30
		IDFModel idfModel = new IDF(30).fit(tfidfData.values());
		tfidfData = tfidfData.mapValues(v -> idfModel.transform(v));
		logger.info("LDA corpus created");
		return tfidfData;
	}

	private JavaPairRDD<Long, List<String>> loadAndPrepareData() {
		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = articlesReader.readArticlesToRDD();
		data = data.filter(a -> a.getText() != null && !a.getText().isEmpty());
		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		return JavaPairRDD.fromJavaRDD(data.map(r -> new Tuple2<Long, List<String>>(r.getId().longValue(), tokenizer.tokenize(r.getText())))
				.filter(a -> !a._2.isEmpty()));
	}

	private Multimap<Integer, String> createMapping(JavaPairRDD<Long, List<String>> data) {
		// Mapowanie wektorow TF na słowa
		JavaRDD<String> tokens = data.values().flatMap(t -> t).distinct();
		logger.info("Tokens count: " + tokens.count());
		return Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));
	}

	private List<Topic> getTopics(DistributedLDAModel ldaModel, Multimap<Integer, String> mapping) {
		// Opisanie topicow za pomoca slow wraz z wagami
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(20);
		topicsDescriptionWriter.writeToFile(d, mapping);
		return topicsDescriptionWriter.convertToTopic(d, mapping);
	}

	private List<TopicArticle> getTopicsArticles(DistributedLDAModel ldaModel) {
		// Opisanie dokumentow za pomoca topicow wraz z wagami
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		// TopicsDistributionWriter.writeToFile(td);
		return topicsDistributionWriter.convertToTopicArticle(td);
	}

	private void saveResults(List<Topic> topics, List<TopicArticle> topicsArticles) {
		topicsDao.deleteAll();
		topicsDao.insert(topics);
		topicsArticlesDao.deleteAll();
		topicsArticlesDao.insert(topicsArticles);
	}
}