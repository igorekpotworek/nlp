package pl.edu.agh.nlp.spark.algorithms;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.BLAS;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.JdbcRDD;

import pl.edu.agh.nlp.DataCleaner;
import pl.edu.agh.nlp.model.ArticleMapper;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.PostgresConnection;
import pl.edu.agh.nlp.spark.utils.Tokenizer;
import scala.Tuple2;

public class FindSimilar {
	private final static Tokenizer tokenizer = new Tokenizer();

	public static void main(String[] args) {

		JavaSparkContext jsc = SparkContextFactory.getJavaSparkContext();

		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = JdbcRDD.create(jsc, new PostgresConnection(),
				"select * from articles where  ? <= id AND id <= ?", 1, 1000, 2, new ArticleMapper());
		data = data.filter(f -> f.getText() != null);

		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		JavaPairRDD<Long, List<String>> javaRdd = JavaPairRDD.fromJavaRDD(data.map(
				r -> new Tuple2<Long, List<String>>(r.getId(), tokenizer.tokenize(DataCleaner.clean(r.getText()))))
				.filter(a -> !a._2.isEmpty()));

		// Budowa modelu TF
		HashingTF hashingTF = new HashingTF(2000);
		JavaPairRDD<Long, Vector> tfData = javaRdd.mapValues(f -> hashingTF.transform(f));

		// Budowa modelu IDF
		IDFModel idfModel = new IDF().fit(tfData.values());
		JavaPairRDD<Long, Vector> tfidfData = tfData.mapValues(v -> idfModel.transform(v));
		JavaRDD<Vector> corpus = tfidfData.values();
		corpus.cache();
		KMeansModel clusters = KMeans.train(corpus.rdd(), 10, 10);
		System.out.println("Model zbudowany");

		JavaPairRDD<Long, Integer> idsWithClustersNumbers = tfidfData.keys().zip(clusters.predict(corpus));
		System.out.println("Korpus przeliczony");

		String txt = "Pod koniec czerwca odby³ siê test przypominaj¹cego swoim wygl¹dem lataj¹cy talerz urz¹dzenia o nazwie Low-Density Supersonic Decelerator (LDSD). O tym, ze test zakoñczy³ siê sukcesem wiedziano ju¿ tego samego dnia, ale dopiero niedawno NASA opublikowa³a nagranie z kamer zainstalowanych na samym pojeŸdzie.";
		Vector txtAsVector = idfModel.transform(hashingTF.transform(tokenizer.tokenize(DataCleaner.clean(txt))));
		int p = clusters.predict(txtAsVector);

		Normalizer n = new Normalizer();
		Vector norTxtAsVector = n.transform(txtAsVector);

		Map<Long, Double> wynik = idsWithClustersNumbers.filter(f -> f._2 == p).join(tfidfData)
				.mapValues(t -> BLAS.dot(norTxtAsVector, n.transform(t._2))).collectAsMap();

		System.out.println(sortByValue(wynik));
		System.out.println(BLAS.dot(norTxtAsVector, n.transform(tfidfData.lookup(10l).get(0))));
	}

	public void countCosine(Vector v1, Vector v2) {
		Normalizer n = new Normalizer();
		System.out.println(BLAS.dot(n.transform(v1), n.transform(v2)));
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}
}
