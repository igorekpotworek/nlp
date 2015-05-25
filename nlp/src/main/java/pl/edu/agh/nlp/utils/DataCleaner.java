package pl.edu.agh.nlp.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.postgresqlDao.ArticlesDaoPostgresql;

@Service
public class DataCleaner {
	@Autowired
	private ArticlesDaoPostgresql articlesDao;

	private static final Logger logger = Logger.getLogger(DataCleaner.class);

	private static final Pattern regex1 = Pattern.compile("Pozostało znaków.*", Pattern.DOTALL);
	private static final Pattern regex2 = Pattern.compile("Tagi\\:.*", Pattern.DOTALL);
	private static final Pattern regex3 = Pattern.compile("\\(fot.*\\)");
	private static final Pattern regex4 = Pattern.compile("Polub WP Sport na Facebooku.*", Pattern.DOTALL);
	private static final Pattern regex5 = Pattern.compile("\\| dodane \\d\\d\\d\\d-\\d\\d-\\d\\d .* temu");
	private static final Pattern regex6 = Pattern.compile("\\‹ poprzednia .* następna \\›.*", Pattern.DOTALL);
	private static final Pattern regex7 = Pattern.compile("Polecamy na Fakt.*", Pattern.DOTALL);
	private static final Pattern regex8 = Pattern.compile("Opracował\\: .*", Pattern.DOTALL);

	private static final Pattern[] patterns = { regex1, regex2, regex3, regex4, regex5, regex6, regex7, regex8 };

	public static String clean(String phrase) {
		if (phrase != null) {
			for (int i = 0; i < patterns.length; i++)
				phrase = patterns[i].matcher(phrase).replaceAll("");
		}
		return phrase;
	}

	public class ArticlesPredicate implements Predicate<Article> {
		@Override
		public boolean test(Article a) {
			for (int i = 0; i < patterns.length; i++) {
				if (a.getText() != null && patterns[i].matcher(a.getText()).find())
					return true;
				else if (a.getIntro() != null && patterns[i].matcher(a.getIntro()).find())
					return true;
			}
			return false;
		}
	}

	public void cleanArticles() {
		logger.info("Clening started");
		List<Article> articles = articlesDao.findAll();
		logger.info("Data loaded");
		articles = articles.stream().filter(new ArticlesPredicate()).collect(Collectors.toList());

		articles.forEach(a -> {
			a.setIntro(clean(a.getIntro()));
			a.setText(clean(a.getText()));
		});

		logger.info("Cleaning complete: " + articles.size() + " changed articles");
		articlesDao.updateBatch(articles);
		logger.info("Data saved");
	}

	public static void main(String[] args) throws IOException {
		String s = new String(Files.readAllBytes(Paths.get("test.txt")), StandardCharsets.UTF_8);
		System.out.println(clean(s));
	}

}
