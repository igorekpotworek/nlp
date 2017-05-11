package pl.edu.agh.nlp.spark.algorithms.classification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.spark.jdbc.ArticlesReader;
import pl.edu.agh.nlp.text.Tokenizer;

import java.io.Serializable;
import java.util.List;

@Service
@Log4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassificationModelBuilder implements Serializable {

	private static final double[] splitTable = { 0.6, 0.2, 0.2 };
	private static final int NUM_FEATURES = 2000000;

	@NonNull
	private final ArticlesReader articlesReader;
	@NonNull
	private final ClassificationModelEvaluator classificationModelEvaluator;
	@NonNull
	private final ModelValidator modelValidator;

	@NonNull
	private final Tokenizer tokenizer;

	ClassificationModel buildModel() {
		HashingTF hashingTF = new HashingTF(NUM_FEATURES);

		long time1 = System.currentTimeMillis();
		JavaRDD<Article> data = articlesReader.loadAndPrepareData();
		long time2 = System.currentTimeMillis();
		log.info("Time of loading and preparing data: " + (time2 - time1) + "ms");

		time1 = System.currentTimeMillis();
		IDFModel idfModel = buildIDFModel(data, hashingTF);
		JavaRDD<LabeledPoint> labeledPoints = data
				.map(a -> new LabeledPoint(a.getCategory().getValue(), transform(a.getText(), hashingTF, idfModel)));
		time2 = System.currentTimeMillis();
		log.info("Time of building TFIDF model: " + (time2 - time1) + "ms");

		JavaRDD<LabeledPoint>[] splits = labeledPoints.randomSplit(splitTable);
		JavaRDD<LabeledPoint> training = splits[0];
		JavaRDD<LabeledPoint> test = splits[1];
		JavaRDD<LabeledPoint> validation = splits[2];

		double bestLambda = modelValidator.validate(training, validation);

		time1 = System.currentTimeMillis();
		NaiveBayesModel model = NaiveBayes.train(training.rdd(), bestLambda);
		time2 = System.currentTimeMillis();
		log.info("Time of building Naive Bayes model: " + (time2 - time1) + "ms");

		classificationModelEvaluator.evaluate(model, test);

		return ClassificationModel.builder()
				.hashingTF(hashingTF)
				.idfModel(idfModel)
				.tokenizer(tokenizer)
				.model(model)
				.build();
	}

	private Vector transform(String text, HashingTF hashingTF, IDFModel idfModel) {
		return idfModel.transform(hashingTF.transform(tokenizer.tokenize(text)));
	}

	private IDFModel buildIDFModel(JavaRDD<Article> data, HashingTF hashingTF) {
		JavaRDD<List<String>> javaRdd = data.map(r -> tokenizer.tokenize(r.getText()))
				.filter(a -> !a.isEmpty());
		JavaRDD<Vector> tfData = hashingTF.transform(javaRdd);
		return new IDF().fit(tfData);
	}
}
