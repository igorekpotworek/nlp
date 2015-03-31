package pl.edu.agh.nlp.utils;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Stemmer {

	public static void stem() throws IOException {
		Analyzer analyzer = new MorfologikAnalyzer();
		String text = "Your cluster’s operation can hiccup because of any of a myriad set of reasons from bugs in HBase itself through misconfigurations — misconfiguration of HBase but also operating system misconfigurations — through to hardware problems whether it be a bug in your network card drivers or an underprovisioned RAM bus (to mention two recent examples of hardware issues that manifested as . You will also need to do a recalibration if up to this your computing has been bound to a single box. Here is one good starting point:";
		TokenStream ts = analyzer.tokenStream("fieldName", text);
		ts.reset();

		while (ts.incrementToken()) {
			CharTermAttribute ca = ts.getAttribute(CharTermAttribute.class);

			System.out.println(ca.toString());
		}
		analyzer.close();
	}

	public static void main(String[] args) throws IOException {
		/*
		 * String text =
		 * "partyjni koledzy Mariusza Kamińskiego, który został skazany na trzy lata więzienia za przekroczenie uprawnień na stanowisku szefa CBA, stają w jego obronie"
		 * ; PolishWordTokenizer t = new PolishWordTokenizer(); for (String word : t.tokenize(text)) { Dictionary polish =
		 * Dictionary.getForLanguage("pl"); DictionaryLookup dl = new DictionaryLookup(polish); List<WordData> wordList =
		 * dl.lookup(word.toLowerCase()); CharSequence stemmed = wordList.isEmpty() ? null : wordList.get(0).getStem();
		 * System.out.println(stemmed); }
		 */
		stem();

	}
}
