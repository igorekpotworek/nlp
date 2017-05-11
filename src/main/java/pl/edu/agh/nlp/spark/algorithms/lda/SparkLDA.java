package pl.edu.agh.nlp.spark.algorithms.lda;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;
import pl.edu.agh.nlp.model.services.TopicsService;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import pl.edu.agh.nlp.text.Tokenizer;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j
public class SparkLDA implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2830677796836853217L;

	private static final HashingTF hashingTF = new HashingTF(1000000);

	@NonNull
	private final Tokenizer tokenizer;

	@NonNull
	private final transient TopicsService topicsService;


	@NonNull
	private final ArticlesReader articlesReader;

	public void buildModel() {
		long time1 = System.currentTimeMillis();
		JavaPairRDD<Long, List<String>> data = loadAndPrepareData();
		long time2 = System.currentTimeMillis();
		log.info("Time of loading and preparing data: " + (time2 - time1) + "ms");

		time1 = System.currentTimeMillis();
		Multimap<Integer, String> mapping = createMapping(data);
		time2 = System.currentTimeMillis();
		log.info("Time of creating mapping: " + (time2 - time1) + "ms");

		time1 = System.currentTimeMillis();
		JavaPairRDD<Long, Vector> corpus = bulidCorpus(data);
		corpus.cache();

		time2 = System.currentTimeMillis();
		log.info("Time of building TFIDF model: " + (time2 - time1) + "ms");

		time1 = System.currentTimeMillis();
		DistributedLDAModel ldaModel = new LDA().setK(100).setAlpha(1.01).run(corpus);
		time2 = System.currentTimeMillis();
		log.info("Time of building LDA model: " + (time2 - time1) + "ms");

		topicsService.save(getTopics(ldaModel, mapping), getTopicsArticles(ldaModel));
		log.info("Model ready");

	}

	private JavaPairRDD<Long, Vector> bulidCorpus(JavaPairRDD<Long, List<String>> data) {
		JavaPairRDD<Long, Vector> tfidfData = data.mapValues(hashingTF::transform);
		IDFModel idfModel = new IDF(30).fit(tfidfData.values());
		tfidfData = tfidfData.mapValues(idfModel::transform);
		log.info("LDA corpus created");
		return tfidfData;
	}

	private JavaPairRDD<Long, List<String>> loadAndPrepareData() {
		JavaRDD<Article> data = articlesReader.readArticlesToRDD();
		data = data.filter(a -> a.getText() != null && !a.getText().isEmpty());
		return JavaPairRDD.fromJavaRDD(data.map(r -> new Tuple2<>(r.getId().longValue(), tokenizer.tokenize(r.getText())))
				.filter(a -> !a._2.isEmpty()));
	}

	private Multimap<Integer, String> createMapping(JavaPairRDD<Long, List<String>> data) {
		JavaRDD<String> tokens = data.values().flatMap(t -> t).distinct();
		log.info("Tokens count: " + tokens.count());
		return Multimaps.index(tokens.collect(), hashingTF::indexOf);
	}

	private List<Topic> getTopics(DistributedLDAModel ldaModel, Multimap<Integer, String> mapping) {
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(20);
		return new TopicsDescriptionWriter().apply(d, mapping);
	}

	private List<TopicArticle> getTopicsArticles(DistributedLDAModel ldaModel) {
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().collect();
		return new TopicsDistributionWriter().apply(td);
	}


}