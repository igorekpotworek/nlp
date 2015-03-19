package pl.edu.agh.nlp.spark;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.languagetool.tokenizers.pl.PolishWordTokenizer;

public class Tokenizer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PolishWordTokenizer tokenizer;
	private List<String> stopWords;

	private final static String PATTERN = "[a-z¹æê³ñóœŸ¿]*";
	private final static String STOP_WORDS_FILE = "stopwords.txt";

	public Tokenizer() {
		tokenizer = new PolishWordTokenizer();
		try {
			stopWords = Files.readAllLines(Paths.get(STOP_WORDS_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> tokenize(String sentence) {
		return tokenizer.tokenize(sentence).stream().map(t -> t.toLowerCase()).filter(t -> t.matches(PATTERN)).filter(t -> t.length() > 1)
				.filter(t -> !stopWords.contains(t)).collect(Collectors.toList());
	}
}
