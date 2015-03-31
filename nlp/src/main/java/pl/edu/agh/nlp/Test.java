package pl.edu.agh.nlp;

import java.util.Collections;

import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.BLAS;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class Test {
	public static void main(String[] args) {
		Vector v1 = Vectors.dense(1.0, 0.0, 3.0);
		Vector v2 = Vectors.dense(1.0, 0.0, 5.0);
		Normalizer n = new Normalizer();
		System.out.println(BLAS.dot(n.transform(v1), n.transform(v2)));
		System.out.println(Collections.singletonMap("asdasf", "safa"));

	}
}
