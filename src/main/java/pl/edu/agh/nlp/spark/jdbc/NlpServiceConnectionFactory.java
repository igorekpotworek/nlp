package pl.edu.agh.nlp.spark.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.spark.rdd.JdbcRDD;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class NlpServiceConnectionFactory implements JdbcRDD.ConnectionFactory, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8564699495854982022L;
	private static ApplicationContext CONTEXT;

	@Override
	public Connection getConnection() throws Exception {
		return CONTEXT.getBean(DataSource.class).getConnection();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		NlpServiceConnectionFactory.CONTEXT = applicationContext;
	}
}
