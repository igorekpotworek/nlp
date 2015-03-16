package pl.edu.agh.nlp.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;

public class SynonymsFinder {
	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName("Synonyms").setMaster(
				"local[2]");
		JavaSparkContext sc = new JavaSparkContext(sparkConf);
		SQLContext sqlContext = new SQLContext(sc);
		DataFrame jdbcDF = new JdbcLoader().getTableFromJdbc(sqlContext, "TMP");
		JavaRDD<Row> data = jdbcDF.javaRDD();

		JavaRDD<List<String>> javaRdd = data.map(r -> new ArrayList<String>(
				Arrays.asList(r.getString(4).trim().split(" "))));

		Word2Vec word2vec = new Word2Vec();
		Word2VecModel model = word2vec.fit(javaRdd);

		Tuple2<String, Object>[] t = model.findSynonyms("tusk", 40);
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]._1);
			System.out.println(t[i]._2);
		}

	}
}
