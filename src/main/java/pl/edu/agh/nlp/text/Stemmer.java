package pl.edu.agh.nlp.text;

import lombok.SneakyThrows;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class Stemmer implements Serializable {

	@SneakyThrows(IOException.class)
	List<String> stem(String text) {
		List<String> stems = new ArrayList<>();
		try (Analyzer analyzer = new MorfologikAnalyzer()) {
			TokenStream ts = analyzer.tokenStream("fieldName", text);
			ts.reset();
			while (ts.incrementToken())
				stems.add(ts.getAttribute(CharTermAttribute.class).toString());
		}
		return stems;
	}
}
