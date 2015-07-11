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

import pl.edu.agh.nlp.model.dao.TopicsArticlesDao;
import pl.edu.agh.nlp.model.dao.TopicsDao;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import pl.edu.agh.nlp.utils.Tokenizer;
import scala.Tuple2;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

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
		JavaPairRDD<Long, List<String>> data = loadAndPrepareData();
		Multimap<Integer, String> mapping = createMapping(data);
		// Budowa modelu
		JavaPairRDD<Long, Vector> corpus = bulidCorpus(data);
		corpus.cache();
		DistributedLDAModel ldaModel = new LDA().setK(100).setAlpha(1.01).run(corpus);
		saveResults(ldaModel, mapping);
		logger.info("LDA model ready");
	}

	private JavaPairRDD<Long, Vector> bulidCorpus(JavaPairRDD<Long, List<String>> data) {
		// Budowa modelu TF
		JavaPairRDD<Long, Vector> tfData = data.mapValues(f -> hashingTF.transform(f));
		// Budowa modelu IDF, minimalna ilość wystapien - 30
		IDFModel idfModel = new IDF(30).fit(tfData.values());
		JavaPairRDD<Long, Vector> tfidfData = tfData.mapValues(v -> idfModel.transform(v));
		logger.info("LDA corpus created");
		return tfidfData;
	}

	private JavaPairRDD<Long, List<String>> loadAndPrepareData() {
		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = articlesReader.readArticlesToRDD();
		data = data.filter(a -> a.getText() != null && !a.getText().isEmpty());
		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		return JavaPairRDD.fromJavaRDD(data
				.map(r -> new Tuple2<Long, List<String>>(r.getId().longValue(), tokenizer.tokenize(r.getText()))).filter(
						a -> !a._2.isEmpty()));
	}

	private Multimap<Integer, String> createMapping(JavaPairRDD<Long, List<String>> data) {
		// Mapowanie wektorow TF na słowa
		JavaRDD<String> tokens = data.values().flatMap(t -> t).distinct();
		logger.info("Tokens count: " + tokens.count());
		return Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));
	}

	private void saveResults(DistributedLDAModel ldaModel, Multimap<Integer, String> mapping) {
		// Opisanie topicow za pomoca slow wraz z wagami
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(20);
		// TopicsDescriptionWriter.writeToFile(d, mapping);
		logger.info("Saving Data to database");
		topicsDao.deleteAll();
		topicsDao.insert(topicsDescriptionWriter.convertToTopic(d, mapping));

		topicsArticlesDao.deleteAll();
		// Opisanie dokumentow za pomoca topicow wraz z wagami
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		topicsArticlesDao.insert(topicsDistributionWriter.convertToTopicArticle(td));
		// TopicsDistributionWriter.writeToFile(td);
	}

}
