package pl.edu.agh.nlp.spark.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.spark.rdd.JdbcRDD;

public class PostgresConnection implements JdbcRDD.ConnectionFactory {

	private static final long serialVersionUID = -637258722756738719L;
	private static final String URL = "jdbc:postgresql://127.0.0.1:6543/postgres";
	// private static final String URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
	private static final String LOGIN = "postgres";
	// private static final String PASSWORD = "soi";
	private static final String PASSWORD = "postgres";

	@Override
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection(URL, LOGIN, PASSWORD);
	}

}
