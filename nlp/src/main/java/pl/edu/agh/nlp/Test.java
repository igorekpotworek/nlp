package pl.edu.agh.nlp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import pl.edu.agh.nlp.spark.algorithms.classification.SparkClassification;

public class Test {
	static ApplicationContext context = new FileSystemXmlApplicationContext(
			"C:\\Users\\Igor\\git\\nlpservice\\nlp\\src\\main\\webapp\\WEB-INF\\spring\\root-context.xml");

	public static void main(String[] args) {
		context.getBean(SparkClassification.class).builidModel();

	}
}
