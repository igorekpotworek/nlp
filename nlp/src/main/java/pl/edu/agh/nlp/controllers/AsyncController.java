package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.ner.PersonDetector;
import pl.edu.agh.nlp.spark.algorithms.classification.SparkClassification;
import pl.edu.agh.nlp.spark.algorithms.lda.SparkLDA;
import pl.edu.agh.nlp.spark.algorithms.recommendations.CollaborativeFiltering;

@Service
public class AsyncController {

	@Autowired
	private SparkClassification sparkClassification;
	@Autowired
	private PersonDetector personDetector;
	@Autowired
	private SparkLDA sparkLDA;
	@Autowired
	private CollaborativeFiltering collaborativeFiltering;

	@Async
	public void buildClassificationModelAsync() {
		sparkClassification.buildModel();
	}

	@Async
	public void buildNERModelAsync() {
		personDetector.buildModel();
	}

	@Async
	public void buildLDAModelAsync() {
		sparkLDA.buildModel();
	}

	@Async
	public void buildRecommenderModel() {
		collaborativeFiltering.buildModel();
	}

}
