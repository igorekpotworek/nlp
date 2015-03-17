package pl.edu.agh.nlp.spark.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import pl.edu.agh.nlp.spark.SparkContextFactory;
import pl.edu.agh.nlp.spark.jdbc.JdbcLoader;
import scala.Tuple2;

public class SynonymsFinder {

	public void find() {
		SparkContext sparkContext = SparkContextFactory.getSparkContext();
		JavaSparkContext sc = new JavaSparkContext(sparkContext);
		SQLContext sqlContext = new SQLContext(sc);
		DataFrame jdbcDF = new JdbcLoader().getTableFromJdbc(sqlContext, "ARTYKULY_SPORT");
		JavaRDD<Row> data = jdbcDF.javaRDD();

		JavaRDD<List<String>> javaRdd = data.map(r -> new ArrayList<String>(Arrays.asList(r.getString(3).trim().split(" "))));

		Word2Vec word2vec = new Word2Vec();
		Word2VecModel model = word2vec.fit(javaRdd);

		Tuple2<String, Object>[] t = model.findSynonyms("tusk", 40);
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]._1);
			System.out.println(t[i]._2);
		}
	}

	public static void main(String[] args) {

	}
}
