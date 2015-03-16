package pl.edu.agh.nlp.spark;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class JdbcLoader {

	public DataFrame getTableFromJdbc(SQLContext sqlContext, String tableName) {
		Map<String, String> options = new HashMap<String, String>();
		options.put("url",
				"jdbc:postgresql://127.0.0.1:6543/postgres?user=postgres&password=postgres");
		options.put("dbtable", tableName);
		return sqlContext.load("jdbc", options);
	}
}
