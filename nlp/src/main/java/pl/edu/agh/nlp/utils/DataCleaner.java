package pl.edu.agh.nlp.utils;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.model.dao.ArticlesDao;
import pl.edu.agh.nlp.model.entities.Article;

@Service
public class DataCleaner {
	@Autowired
	private ArticlesDao articlesDao;

	private static final Logger logger = Logger.getLogger(DataCleaner.class);

	private static final Pattern regex1 = Pattern.compile("Pozosta³o znaków: 4000 Zaloguj siê Twój podpis.*", Pattern.DOTALL);
	private static final Pattern regex2 = Pattern.compile("Tagi\\:.*", Pattern.DOTALL);
	private static final Pattern regex3 = Pattern.compile("\\(fot.*\\)");

	private static final Pattern[] patterns = { regex1, regex2, regex3 };

	public static String clean(String phrase) {
		for (int i = 0; i < patterns.length; i++)
			phrase = patterns[i].matcher(phrase).replaceAll("");
		return phrase;
	}

	public void cleanArticles() {
		logger.info("Clening started");
		List<Article> articles = articlesDao.findAll();
		logger.info("Data loaded");

		/*
		 * articles = articles .stream() .filter(a -> a.getText().contains("Pozosta³o znaków: 4000 Zaloguj siê Twój podpis") ||
		 * a.getText().contains("(fot") || a.getText().contains("Tagi:") ||
		 * a.getIntro().contains("Pozosta³o znaków: 4000 Zaloguj siê Twój podpis") || a.getIntro().contains("(fot") ||
		 * a.getIntro().contains("Tagi:")).collect(Collectors.toList());
		 */
		articles.forEach(a -> {
			a.setIntro(clean(a.getIntro()));
			a.setText(clean(a.getText()));
		});

		logger.info("Cleaning complete");

		articlesDao.updateBatch(articles);
		logger.info("Data saved");
	}

	public static void main(String[] args) {
		logger.info("Data saved");

	}

}
