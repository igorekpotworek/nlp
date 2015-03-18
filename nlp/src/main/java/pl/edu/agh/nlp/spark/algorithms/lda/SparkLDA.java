package pl.edu.agh.nlp.spark.algorithms.lda;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.DistributedLDAModel;
import org.apache.spark.mllib.clustering.LDA;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.JdbcRDD;
import org.languagetool.tokenizers.pl.PolishWordTokenizer;

import pl.edu.agh.nlp.model.Article;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import scala.Tuple2;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class SparkLDA {

	private final static PolishWordTokenizer tokenizer = new PolishWordTokenizer();

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		JavaSparkContext jsc = SparkContextFactory.getJavaSparkContext();

		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select tekst from ARTYKULY_WIADOMOSCI where  ? <= id AND id <= ?", 1, 5000, 10, new ArticleMapper());

		JavaRDD<List<String>> javaRdd = data.map(r -> tokenizer.tokenize(r.getText()));

		JavaRDD<String> tokens = javaRdd.flatMap(t -> t).distinct();

		HashingTF hashingTF = new HashingTF((int) tokens.count());

		JavaRDD<Vector> parsedData = hashingTF.transform(javaRdd);
		Multimap<Integer, String> mapping = Multimaps.index(tokens.toArray(), t -> hashingTF.indexOf(t));

		JavaPairRDD<Long, Vector> corpus = JavaPairRDD.fromJavaRDD(parsedData.zipWithIndex().map(t -> t.swap()));

		corpus.cache();

		DistributedLDAModel ldaModel = new LDA().setK(3).run(corpus);

		Tuple2<int[], double[]>[] d = ldaModel.describeTopics(10);
		TopicsDescriptionWriter.writeToFile(d, mapping);

		List<Tuple2<Object, Vector>> td = ldaModel.topicDistributions().toJavaRDD().toArray();
		TopicsDistributionWriter.writeToFile(td);

	}
}
