package pl.edu.agh.nlp.spark.algorithms;

import java.io.IOException;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.ModelFilesManager;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.algorithms.lda.TopicsDescriptionWriter;
import pl.edu.agh.nlp.spark.algorithms.lda.TopicsDistributionWriter;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import pl.edu.agh.nlp.spark.utils.Tokenizer;
import scala.Tuple2;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class SparkLDA {
	private final static Tokenizer tokenizer = new Tokenizer();

	public DistributedLDAModel bulidModel() throws IOException {
		JavaSparkContext jsc = SparkContextFactory.getJavaSparkContext();

		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(), "select * from articles where  ? <= id AND id <= ?", 1, 1000,
				10, new ArticleMapper());
		data = data.filter(f -> f.getText() != null);

		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		JavaRDD<List<String>> javaRdd = data.map(r -> tokenizer.tokenize(r.getText())).filter(a -> !a.isEmpty());

		// Budowa modelu TF
		HashingTF hashingTF = new HashingTF(2000000);
		JavaRDD<Vector> tfData = hashingTF.transform(javaRdd);

		// Mapowanie wektorow TF na s³owa
		JavaRDD<String> tokens = javaRdd.flatMap(t -> t).distinct();
		Multimap<Integer, String> mapping = Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));

		// Budowa modelu IDF
		IDFModel idfModel = new IDF().fit(tfData);
		JavaRDD<Vector> tfidfData = idfModel.transform(tfData);

		// Przygotowanie korpusu
		JavaPairRDD<Long, Vector> corpus = JavaPairRDD.fromJavaRDD(tfidfData.zipWithIndex().map(t -> t.swap()));

		// Budowa modelu
		DistributedLDAModel ldaModel = new LDA().setK(20).run(corpus);

		// Serializacja modelu
		ModelFilesManager modelFilesManager = new ModelFilesManager();
		modelFilesManager.saveModel(ldaModel.toLocal(), "lda_model.o");

		// Opisanie topicow za pomoca slow wraz z wagami
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(20);
		TopicsDescriptionWriter.writeToFile(d, mapping);

		// Opisanie dokumentow za pomoca topicow wraz z wagami
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		TopicsDistributionWriter.writeToFile(td);
		return ldaModel;

	}
}
