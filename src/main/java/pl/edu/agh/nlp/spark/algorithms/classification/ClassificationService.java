package pl.edu.agh.nlp.spark.algorithms.classification;

import pl.edu.agh.nlp.model.entities.Article.Category;

public interface ClassificationService {

	void buildModel();

	boolean isPresent();

	Category predictCategory(String text);
}
