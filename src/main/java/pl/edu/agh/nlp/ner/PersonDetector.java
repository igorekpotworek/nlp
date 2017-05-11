package pl.edu.agh.nlp.ner;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.exceptions.AbsentModelException;

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

@Service
@Log4j
public class PersonDetector {

	private static final String MODEL_FILE_NAME = "pl-ner-person.bin";
	private static final String MODEL_TRAIN_FILE_NAME = "pl-ner-person.train";

	private NameFinderME nameFinder;

	@NonNull
	private final SentenceDetector sentenceDetector;
	@NonNull
	private final NERModelEvaluator nerModelEvaluator;

	@Autowired
	public PersonDetector(@NonNull final SentenceDetector sentenceDetector, @NonNull final NERModelEvaluator nerModelEvaluator) {
		this.sentenceDetector = sentenceDetector;
		this.nerModelEvaluator = nerModelEvaluator;
		try (InputStream modelIn = new ClassPathResource(MODEL_FILE_NAME).getInputStream()) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			nameFinder = new NameFinderME(model);
		} catch (FileNotFoundException e) {
			log.error("No corpus file", e);
		} catch (IOException e) {
			log.error("Invalid Corpus File", e);
		}
	}

	public void buildModel() {
		try {
			Charset charset = Charset.forName("UTF-8");
			ObjectStream<String> lineStream = new PlainTextByLineStream(new ClassPathResource(MODEL_TRAIN_FILE_NAME).getInputStream(),
					charset);
			ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
			trainModel(sampleStream);
			nerModelEvaluator.evaluateModel(nameFinder);
		} catch (IOException e) {
			log.error("No training data file", e);
		}
	}

	private void trainModel(ObjectStream<NameSample> sampleStream) {
		try {
			TokenNameFinderModel model = NameFinderME.train("pl", "person", sampleStream, Collections.emptyMap());
			try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(MODEL_FILE_NAME))) {
				model.serialize(modelOut);
			}
			nameFinder = new NameFinderME(model);
		} catch (IOException e) {
			log.error("Model Training Failed", e);
		}
	}

	public List<String> detect(String document) {
		if (nameFinder == null) {
			throw new AbsentModelException();
		} else {
			List<String> detectedPersons = new ArrayList<>();
			List<String> sentences = sentenceDetector.detectSentences(document);
			for (String sentence : sentences) {
				String[] tokens = sentence.split("\\s+");
				Span[] nameSpans = nameFinder.find(tokens);
				if (nameSpans.length > 0) {
					for (Span nameSpan : nameSpans) {
						String person = "";
						for (int j = nameSpan.getStart(); j < nameSpan.getEnd(); j++) {
							person += tokens[j] + " ";
						}
						detectedPersons.add(person);
					}
				}
			}
			nameFinder.clearAdaptiveData();
			return detectedPersons;
		}
	}


}
