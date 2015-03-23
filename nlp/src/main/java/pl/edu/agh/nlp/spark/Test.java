package pl.edu.agh.nlp.spark;

import java.io.IOException;

import pl.edu.agh.nlp.spark.algorithms.SparkLDA;

public class Test {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		new SparkLDA().getModel();
	}
}
