package pl.edu.agh.nlp.utils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public class Tokenizer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Stemmer stemmer;
	private List<String> stopWords;

	private final static String PATTERN = "[a-ząćęłńóśźż]*";
	private final static String STOP_WORDS_FILE = "stopwords.txt";

	public Tokenizer() {
		stemmer = new Stemmer();
		try {
			stopWords = Files.readAllLines(Paths.get(new ClassPathResource(STOP_WORDS_FILE).getFile().getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> tokenize(String sentence) {
		return stemmer.stem(sentence).stream().map(t -> t.toLowerCase()).filter(t -> t.matches(PATTERN)).filter(t -> t.length() > 1)
				.filter(t -> !stopWords.contains(t)).collect(Collectors.toList());
	}
}
