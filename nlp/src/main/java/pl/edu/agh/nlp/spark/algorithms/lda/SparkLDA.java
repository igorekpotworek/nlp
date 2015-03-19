package pl.edu.agh.nlp.spark.algorithms.lda;

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

import pl.edu.agh.nlp.model.Article;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.Tokenizer;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import scala.Tuple2;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class SparkLDA {
	private final static Tokenizer tokenizer = new Tokenizer();

	public static void main(String[] args) throws IOException {

		JavaSparkContext jsc = SparkContextFactory.getJavaSparkContext();

		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select tekst from ARTYKULY_WIADOMOSCI where  ? <= id AND id <= ?", 1, 10000, 10, new ArticleMapper());

		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		JavaRDD<List<String>> javaRdd = data.map(r -> tokenizer.tokenize(r.getText()));

		// Wy
		JavaRDD<String> tokens = javaRdd.flatMap(t -> t).distinct();
		HashingTF hashingTF = new HashingTF(2000000);

		JavaRDD<Vector> tfData = hashingTF.transform(javaRdd);
		Multimap<Integer, String> mapping = Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));

		IDFModel idfModel = new IDF().fit(tfData);
		JavaRDD<Vector> tfidfData = idfModel.transform(tfData);

		JavaPairRDD<Long, Vector> corpus = JavaPairRDD.fromJavaRDD(tfidfData.zipWithIndex().map(t -> t.swap()));

		corpus.cache();

		// Budowa modelu
		DistributedLDAModel ldaModel = new LDA().setK(20).run(corpus);

		// Opisanie topicow za pomoca slow wraz z wagami
		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(20);
		TopicsDescriptionWriter.writeToFile(d, mapping);

		// Opisanie dokumentow za pomoca topicow wraz z wagami
		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		TopicsDistributionWriter.writeToFile(td);

	}
}
