package pl.edu.agh.nlp.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Stemmer {

	public List<String> stem(String text) {
		Analyzer analyzer = new MorfologikAnalyzer();
		List<String> stems = new ArrayList<String>();
		try {
			TokenStream ts = analyzer.tokenStream("fieldName", text);
			ts.reset();
			while (ts.incrementToken())
				stems.add(ts.getAttribute(CharTermAttribute.class).toString());
		} catch (IOException e) {
			return null;
		} finally {
			analyzer.close();
		}
		return stems;
	}

}
