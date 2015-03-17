package pl.edu.agh.nlp.spark.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.spark.rdd.JdbcRDD;

public class PostgresConnection implements JdbcRDD.ConnectionFactory {

	private static final long serialVersionUID = -637258722756738719L;

	@Override
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:6543/postgres", "postgres", "postgres");
	}

}
