package pl.edu.agh.nlp.spark;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.edu.agh.nlp.model.dao.ArticlesDao;

public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-config.xml");
		ArticlesDao articlesDao = context.getBean(ArticlesDao.class);
		System.out.println(articlesDao.searchArticles("Milik"));
	}
}
