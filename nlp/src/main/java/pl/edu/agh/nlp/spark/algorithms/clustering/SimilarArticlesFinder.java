package pl.edu.agh.nlp.spark.algorithms.clustering;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.BLAS;
import org.apache.spark.mllib.linalg.Vector;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.algorithms.classification.SparkClassification;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import pl.edu.agh.nlp.utils.DataCleaner;
import pl.edu.agh.nlp.utils.Tokenizer;
import scala.Tuple2;

public class SimilarArticlesFinder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Tokenizer tokenizer = new Tokenizer();
	private static final Logger logger = Logger.getLogger(SparkClassification.class);

	private KMeansModel kMeansModel;
	private static final HashingTF hashingTF = new HashingTF(2000);

	private IDFModel idfModel;
	private JavaPairRDD<Long, Integer> idsWithClustersNumbers;
	private JavaPairRDD<Long, Vector> tfidfData;

	private static SimilarArticlesFinder instance;

	public static synchronized SimilarArticlesFinder getSimilarArticlesFinder() {
		if (instance == null)
			instance = new SimilarArticlesFinder();
		return instance;
	}

	private SimilarArticlesFinder() {

	}

	public void builidModel() {
		// Wczytanie danych (artykulow) z bazy danych
		JavaRDD<Article> data = ArticlesReader.readArticlesToRDD();
		data = data.filter(f -> f.getText() != null);

		// Tokenizacja, usuniecie slow zawierajacych znaki specjalne oraz cyfry, usuniecie slow o dlugosci < 2
		JavaPairRDD<Long, List<String>> javaRdd = JavaPairRDD.fromJavaRDD(data.map(
				r -> new Tuple2<Long, List<String>>(r.getId(), tokenizer.tokenize(r.getText()))).filter(a -> !a._2.isEmpty()));

		// Budowa modelu TF
		JavaPairRDD<Long, Vector> tfData = javaRdd.mapValues(f -> hashingTF.transform(f));

		// Budowa modelu IDF
		idfModel = new IDF().fit(tfData.values());
		tfidfData = tfData.mapValues(v -> idfModel.transform(v));
		JavaRDD<Vector> corpus = tfidfData.values();
		corpus.cache();
		kMeansModel = KMeans.train(corpus.rdd(), 1000000, 20);
		kMeansModel.clusterCenters();
		logger.info("Model zbudowany");

		idsWithClustersNumbers = tfidfData.keys().zip(kMeansModel.predict(corpus));
		idsWithClustersNumbers.count();

		// wypisanie wielkosci poszczegolnych klastrow

		Map<Long, Integer> m = idsWithClustersNumbers.collectAsMap();
		Map<Integer, Integer> m2 = new HashMap<Integer, Integer>();

		for (Long l : m.keySet()) {
			Integer i = m.get(l);
			if (m2.containsKey(i)) {
				m2.put(i, m2.get(i) + 1);
			} else {
				m2.put(i, 1);
			}
		}

		for (Integer i : m2.keySet())
			System.out.println("Klaster : " + i + "rozmiar:" + m2.get(i));

		logger.info("Korpus przeliczony");

	}

	public void find(String txt) {
		long time1 = System.currentTimeMillis();
		Vector txtAsVector = idfModel.transform(hashingTF.transform(tokenizer.tokenize(DataCleaner.clean(txt))));
		long time2 = System.currentTimeMillis();
		System.out.println("time1: " + (time2 - time1));

		int p = kMeansModel.predict(txtAsVector);
		long time3 = System.currentTimeMillis();
		System.out.println("time2: " + (time3 - time2));

		Normalizer n = new Normalizer();
		Vector norTxtAsVector = n.transform(txtAsVector);
		long time4 = System.currentTimeMillis();
		System.out.println("time3: " + (time4 - time3));

		Map<Long, Double> wynik = idsWithClustersNumbers.filter(f -> f._2 == p).join(tfidfData)
				.mapValues(t -> BLAS.dot(norTxtAsVector, n.transform(t._2))).collectAsMap();

		System.out.println("size: " + wynik.size());

		long time5 = System.currentTimeMillis();
		System.out.println("time4: " + (time5 - time4));

		System.out.println(sortByValue(wynik));
		long time6 = System.currentTimeMillis();

		System.out.println("time5: " + (time6 - time5));
		System.out.println("total time : " + (time6 - time1));

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

	public static void main(String[] args) throws IOException {
		SimilarArticlesFinder similarArticlesFinder = new SimilarArticlesFinder();
		similarArticlesFinder.builidModel();

		byte[] encoded = Files.readAllBytes(Paths.get("plik.txt"));
		String text = new String(encoded, StandardCharsets.UTF_8);
		System.out.println(text);
		similarArticlesFinder.find(text);
	}
}
