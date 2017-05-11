package pl.edu.agh.nlp.spark.algorithms.classification;

import lombok.Builder;
import lombok.NonNull;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import pl.edu.agh.nlp.model.entities.Article.Category;
import pl.edu.agh.nlp.text.Tokenizer;

import java.util.List;

@Builder
class ClassificationModel {

	@NonNull
	private final Tokenizer tokenizer;
	@NonNull
	private final HashingTF hashingTF;
	@NonNull
	private final NaiveBayesModel model;
	@NonNull
	private final IDFModel idfModel;

	Category predictCategory(String text) {
		final List<String> tokenize = tokenizer.tokenize(text);
		final Vector vector = idfModel.transform(hashingTF.transform(tokenize));
		final int categoryValue = (int) model.predict(vector);
		return Category.fromInt(categoryValue);
	}

}