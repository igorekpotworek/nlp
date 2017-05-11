package pl.edu.agh.nlp.spark.algorithms.classification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@Log4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelValidator implements Serializable{

	private static final double[] LAMBDAS = new double[]{0.00001, 0.0001, 0.001, 0.01, 0.1, 1.0, 10.0, 50.0};

	@NonNull
	private final ClassificationModelEvaluator classificationModelEvaluator;

	double validate(JavaRDD<LabeledPoint> training, JavaRDD<LabeledPoint> validation) {
		double bestValidationEffectiveness = 0.0;
		double bestLambda = -1.0;

		for (double lambda : LAMBDAS) {
			NaiveBayesModel validatedModel = NaiveBayes.train(training.rdd(), lambda);
			double validationEffectiveness = classificationModelEvaluator.evaluate(validatedModel, validation);
			log.info("Effectiveness  (validation) = " + validationEffectiveness + " for the model trained with lambda = " + lambda);

			if (validationEffectiveness > bestValidationEffectiveness) {
				bestValidationEffectiveness = validationEffectiveness;
				bestLambda = lambda;
			}
		}
		log.info("The best model was trained with lambda = " + bestLambda);
		return bestLambda;
	}
}
