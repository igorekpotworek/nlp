package pl.edu.agh.nlp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

public class DataCleaner {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

	public static String clean(String phrase) {
		return phrase.replaceAll("Tagi:.*", "").replaceAll("Pozosta³o znaków: 4000 Zaloguj siê Twój podpis.*", "")
				.replaceAll("(fot.*)", "");
	}

	public static void main(String[] args) {
		ArticlesDao articlesDao = context.getBean(ArticlesDao.class);
		List<Article> articles = articlesDao.findAll();
		System.out.println("Data loaded");

		articles.forEach(a -> {
			a.setIntro(clean(a.getIntro()));
			a.setText(clean(a.getText()));
		});
		articles = articles.stream().filter(a -> (!a.getIntro().contains("Jamie Stokes") && !a.getText().contains("Jamie Stokes")))
				.collect(Collectors.toList());

		System.out.println("Cleaning complete");

		articlesDao.updateBatch(articles);
		System.out.println("Data saved");
	}
}
