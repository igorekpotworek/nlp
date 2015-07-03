package pl.edu.agh.nlp.ner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;

public class PersonDetector {

	private static final String MODEL_FILE_NAME = "en-ner-person.bin";

	public void detect() throws InvalidFormatException, IOException {
		InputStream modelIn = new FileInputStream(MODEL_FILE_NAME);
		TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
		NameFinderME nameFinder = new NameFinderME(model);
	}

}
