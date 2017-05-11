package pl.edu.agh.nlp.spark.algorithms.classification;

import lombok.extern.log4j.Log4j;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.Serializable;

@Service
@Log4j
public class ClassificationModelEvaluator implements Serializable{

	double evaluate(NaiveBayesModel validatedModel, JavaRDD<LabeledPoint> test) {
		log.info("Start evaluating model");
		JavaPairRDD<Double, Double> predictionAndLabel = test
				.mapToPair(p -> new Tuple2<>(validatedModel.predict(p.features()), p.label()));
		long accuracy = predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count();
		double effectiveness = accuracy / (double) test.count();
		log.info("Effectiveness: " + effectiveness);
		return effectiveness;
	}

}
