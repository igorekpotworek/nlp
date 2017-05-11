package pl.edu.agh.nlp.ner;

import lombok.extern.log4j.Log4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.eval.FMeasure;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;

@Service
@Log4j
public class NERModelEvaluator {

	private static final String MODEL_TEST_FILE_NAME = "pl-ner-person.test";

	void evaluateModel(NameFinderME nameFinder) {
		try {
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					new ClassPathResource(MODEL_TEST_FILE_NAME).getInputStream(), Charset.forName("UTF-8"));
			ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
			TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(nameFinder);
			evaluator.evaluate(sampleStream);
			FMeasure result = evaluator.getFMeasure();
			log.info(result.toString());
		} catch (IOException e) {
			log.error("Model Evaluating Failed", e);
		}
	}
}
