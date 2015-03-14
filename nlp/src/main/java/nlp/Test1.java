package nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;

public class Test1 {
	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName("Nlp").setMaster(
				"local[2]");

		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

		Map<String, String> options = new HashMap<String, String>();
		options.put("url",
				"jdbc:postgresql://127.0.0.1:6543/postgres?user=postgres&password=postgres");
		options.put("dbtable", "public.TMP");

		DataFrame jdbcDF = sqlContext.load("jdbc", options);

		JavaRDD<Row> data = jdbcDF.javaRDD();

		JavaRDD<List<String>> javaRdd = data.map(r -> new ArrayList<String>(
				Arrays.asList(r.getString(4).trim().split(" "))));

		Word2Vec word2vec = new Word2Vec();

		Word2VecModel model = word2vec.fit(javaRdd);

		Tuple2<String, Object>[] t = model.findSynonyms("narkotyki", 40);
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]._1);
			System.out.println(t[i]._2);

		}

	}
}
