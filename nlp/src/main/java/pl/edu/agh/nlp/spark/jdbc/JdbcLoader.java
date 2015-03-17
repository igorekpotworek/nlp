package pl.edu.agh.nlp.spark.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class JdbcLoader {

	private static final String URL = "jdbc:postgresql://127.0.0.1:5432/postgres?user=postgres&password=soi";

	public DataFrame getTableFromJdbc(SQLContext sqlContext, String tableName) {
		Map<String, String> options = new HashMap<String, String>();
		options.put("url", URL);
		options.put("dbtable", tableName);
		return sqlContext.load("jdbc", options);
	}
}
