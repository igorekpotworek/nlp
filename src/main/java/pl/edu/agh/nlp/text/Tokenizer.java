package pl.edu.agh.nlp.text;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Log4j
public class Tokenizer implements Serializable {

	private static final String PATTERN = "[a-ząćęłńóśźż]*";
	private static final String STOP_WORDS_FILE = "stopwords.txt";

	@NonNull
	private final Stemmer stemmer;

	private List<String> stopWords;

	@Autowired
	@SneakyThrows(IOException.class)
	public Tokenizer(@NonNull final Stemmer stemmer) {
		this.stemmer = stemmer;
		Path path = Paths.get(new ClassPathResource(STOP_WORDS_FILE).getFile().getAbsolutePath());
		stopWords = Files.readAllLines(path);
	}

	public List<String> tokenize(String sentence) {
		return stemmer.stem(sentence)
				.stream()
				.map(String::toLowerCase)
				.filter(t -> t.matches(PATTERN))
				.filter(t -> t.length() > 1)
				.filter(t -> !stopWords.contains(t))
				.collect(toList());
	}
}
