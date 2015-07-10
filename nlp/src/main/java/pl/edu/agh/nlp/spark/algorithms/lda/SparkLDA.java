package pl.edu.agh.nlp.spark.algorithms.lda;

import java.io.IOException;
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
import org.springframework.scheduling.annotation.Async;
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
	private final static Tokenizer tokenizer = new Tokenizer();
	private DistributedLDAModel ldaModel;
	private Multimap<Integer, String> mapping;

	@Autowired
	private TopicsDao topicsDao;
	@Autowired
	private TopicsArticlesDao topicsArticlesDao;

	@Autowired
	private ArticlesReader articlesReader;

	public void buildModel() throws IOException {

		// Budowa modelu
		JavaPairRDD<Long, Vector> corpus = bulidCorpus();
		corpus.cache();

		// TODO dodac min freq
		ldaModel = new LDA().setK(100).setAlpha(1.01).run(corpus);

		// Opisanie topicow za pomoca slow wraz z wagami
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(20);

		// TopicsDescriptionWriter.writeToFile(d, mapping);
		logger.info("Saving Data to database");
		topicsDao.deleteAll();
		topicsDao.insert(TopicsDescriptionWriter.convertToTopic(d, mapping));

		topicsArticlesDao.deleteAll();
		// Opisanie dokumentow za pomoca topicow wraz z wagami
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		topicsArticlesDao.insert(TopicsDistributionWriter.convertToTopicArticle(td));

		// TopicsDistributionWriter.writeToFile(td);

		logger.info("LDA model ready");
	}

	public JavaPairRDD<Long, Vector> bulidCorpus() {
		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = articlesReader.readArticlesToRDD();
		data = data.filter(f -> f.getText() != null);

		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		JavaPairRDD<Long, List<String>> javaRdd = JavaPairRDD.fromJavaRDD(data.map(
				r -> new Tuple2<Long, List<String>>(r.getId().longValue(), tokenizer.tokenize(r.getText()))).filter(a -> !a._2.isEmpty()));
		// Budowa modelu TF
		JavaPairRDD<Long, Vector> tfData = javaRdd.mapValues(f -> hashingTF.transform(f));

		// Mapowanie wektorow TF na słowa
		JavaRDD<String> tokens = javaRdd.values().flatMap(t -> t).distinct();
		logger.info("Tokens count: " + tokens.count());
		mapping = Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));

		// Budowa modelu IDF
		IDFModel idfModel = new IDF(30).fit(tfData.values());
		JavaPairRDD<Long, Vector> tfidfData = tfData.mapValues(v -> idfModel.transform(v));
		logger.info("LDA corpus created");
		return tfidfData;
	}

	@Async
	public void buildModelAsync() throws IOException {
		buildModel();
	}
}
