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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import pl.edu.agh.nlp.utils.Tokenizer;
import scala.Tuple2;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class SparkLDA implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2830677796836853217L;

	private static ApplicationContext context = new ClassPathXmlApplicationContext("root-context.xml");
	private static final String MODEL_PATH = "models/recomender/model.o";
	private static final Logger logger = Logger.getLogger(SparkLDA.class);

	private final static Tokenizer tokenizer = new Tokenizer();
	private DistributedLDAModel ldaModel;

	public void bulidModel() throws IOException {
		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = ArticlesReader.readArticlesToRDD();

		data = data.filter(f -> f.getText() != null);

		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		JavaPairRDD<Long, List<String>> javaRdd = JavaPairRDD.fromJavaRDD(data.map(
				r -> new Tuple2<Long, List<String>>(r.getId(), tokenizer.tokenize(r.getText()))).filter(a -> !a._2.isEmpty()));

		// Budowa modelu TF
		HashingTF hashingTF = new HashingTF(2000000);
		JavaPairRDD<Long, Vector> tfData = javaRdd.mapValues(f -> hashingTF.transform(f));

		// Mapowanie wektorow TF na słowa
		JavaRDD<String> tokens = javaRdd.values().flatMap(t -> t).distinct();
		Multimap<Integer, String> mapping = Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));

		// Budowa modelu IDF
		IDFModel idfModel = new IDF().fit(tfData.values());
		JavaPairRDD<Long, Vector> tfidfData = tfData.mapValues(v -> idfModel.transform(v));

		// Budowa modelu

		// TODO dodac min freq
		ldaModel = new LDA().setK(100).run(tfidfData);

		// Serializacja modelu
		// ModelFilesManager modelFilesManager = new ModelFilesManager();
		// modelFilesManager.saveModel(ldaModel.toLocal(), "D://models/lda_model.o");

		// Opisanie topicow za pomoca slow wraz z wagami
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(10);
		TopicsDescriptionWriter.writeToFile(d, mapping);
		// context.getBean(TopicsWordsDao.class).insert(TopicsDescriptionWriter.convertToTopicWord(d, mapping));

		// Opisanie dokumentow za pomoca topicow wraz z wagami
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		TopicsDistributionWriter.writeToFile(td);
	}

	public static void main(String[] args) throws IOException {
		SparkLDA l = new SparkLDA();
		l.bulidModel();
	}
}
