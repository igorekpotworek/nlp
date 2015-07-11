package pl.edu.agh.nlp.utils;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class SentenceDetector {
	public List<String> detectSentences(String text) {
		BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(Locale.forLanguageTag("pl_PL"));
		List<String> sentences = new ArrayList<String>();
		sentenceIterator.setText(text);
		int prev = 0;
		int next = sentenceIterator.next();
		while (next != BreakIterator.DONE) {
			sentences.add(text.substring(prev, next));
			prev = next;
			next = sentenceIterator.next();
		}
		return sentences;
	}
}
