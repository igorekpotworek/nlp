package pl.edu.agh.nlp.ner;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.eval.FMeasure;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.utils.SentenceDetector;

@Service
public class PersonDetector {

	private static final String MODEL_FILE_NAME = "pl-ner-person.bin";
	private static final String MODEL_TRAIN_FILE_NAME = "pl-ner-person.train";
	private static final String MODEL_TEST_FILE_NAME = "pl-ner-person.test";

	private static final Logger logger = Logger.getLogger(PersonDetector.class);
	private NameFinderME nameFinder;

	@Autowired
	private SentenceDetector sentenceDetector;

	public PersonDetector() {
		try (InputStream modelIn = new ClassPathResource(MODEL_FILE_NAME).getInputStream()) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			nameFinder = new NameFinderME(model);
		} catch (FileNotFoundException e) {
			logger.error("No corpus file", e);
		} catch (IOException e) {
			logger.error("Invalid Corpus File", e);
		}
	}

	public void buildModel() {
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = null;
		try {
			lineStream = new PlainTextByLineStream(new ClassPathResource(MODEL_TRAIN_FILE_NAME).getInputStream(),
					charset);
		} catch (IOException e) {
			logger.error("No training data file", e);
		}
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
		try {
			TokenNameFinderModel model = NameFinderME.train("pl", "person", sampleStream, Collections.emptyMap());
			try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(MODEL_FILE_NAME))) {
				model.serialize(modelOut);
			}
			nameFinder = new NameFinderME(model);
		} catch (IOException e) {
			logger.error("Model Training Failed", e);
		}
		evaluateModel(nameFinder);
	}

	public List<String> detect(String document) throws AbsentModelException {
		if (nameFinder == null) {
			throw new AbsentModelException();
		} else {
			List<String> detectedPersons = new ArrayList<String>();
			List<String> sentences = sentenceDetector.detectSentences(document);
			for (String sentence : sentences) {
				String[] tokens = sentence.split("\\s+");
				Span[] nameSpans = nameFinder.find(tokens);
				if (nameSpans.length > 0) {
					for (int i = 0; i < nameSpans.length; i++) {
						String person = "";
						for (int j = nameSpans[i].getStart(); j < nameSpans[i].getEnd(); j++)
							person += tokens[j] + " ";
						detectedPersons.add(person);
					}
				}
			}
			nameFinder.clearAdaptiveData();
			return detectedPersons;
		}
	}

	private void evaluateModel(NameFinderME nameFinder) {
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					new ClassPathResource(MODEL_TEST_FILE_NAME).getInputStream(), Charset.forName("UTF-8"));
			ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
			TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(nameFinder);
			evaluator.evaluate(sampleStream);
			FMeasure result = evaluator.getFMeasure();
			logger.info(result.toString());
		} catch (IOException e) {
			logger.error("Model Evaluating Failed", e);

		}
	}
}
