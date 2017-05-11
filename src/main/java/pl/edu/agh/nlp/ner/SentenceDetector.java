package pl.edu.agh.nlp.ner;

import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.text.BreakIterator.DONE;

@Service
public class SentenceDetector {
	
	List<String> detectSentences(String text) {
		BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(Locale.forLanguageTag("pl_PL"));
		sentenceIterator.setText(text);
		int prev = 0;
		int next = sentenceIterator.next();
		List<String> sentences = new ArrayList<>();
		while (next != DONE) {
			sentences.add(text.substring(prev, next));
			prev = next;
			next = sentenceIterator.next();
		}
		return sentences;
	}
}
