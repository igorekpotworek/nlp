package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.ner.PersonDetector;
import pl.edu.agh.nlp.spark.algorithms.classification.ClassificationService;
import pl.edu.agh.nlp.spark.algorithms.lda.SparkLDA;
import pl.edu.agh.nlp.spark.algorithms.recommendations.CollaborativeFiltering;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AsyncController {

	@NonNull
	private final ClassificationService classificationService;
	@NonNull
	private final PersonDetector personDetector;
	@NonNull
	private final SparkLDA sparkLDA;
	@NonNull
	private final CollaborativeFiltering collaborativeFiltering;

	@Async
	void buildClassificationModelAsync() {
		classificationService.buildModel();
	}

	@Async
	void buildNERModelAsync() {
		personDetector.buildModel();
	}

	@Async
	void buildLDAModelAsync() {
		sparkLDA.buildModel();
	}

	@Async
	void buildRecommenderModel() {
		collaborativeFiltering.buildModel();
	}

}
