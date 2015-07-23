package pl.edu.agh.nlp;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.ner.PersonDetector;

public class Test {
	static ApplicationContext context = new FileSystemXmlApplicationContext(
			"C:\\Users\\Igor\\git\\nlpservice\\nlp\\src\\main\\webapp\\WEB-INF\\spring\\root-context.xml");

	public static void main(String[] args) throws BeansException, IOException, AbsentModelException {

		context.getBean(PersonDetector.class).buildModel();
		// System.out.println(context.getBean(PersonDetector.class).detect(FileUtils.readFileToString(new File("test.txt"))));

	}

}
