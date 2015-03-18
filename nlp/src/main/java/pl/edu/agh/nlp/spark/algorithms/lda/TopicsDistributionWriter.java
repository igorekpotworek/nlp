package pl.edu.agh.nlp.spark.algorithms.lda;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.spark.mllib.linalg.Vector;

import scala.Tuple2;

public class TopicsDistributionWriter {
	public static void writeToFile(List<Tuple2<Object, Vector>> td) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("TOPICS_DISTRIBUTION.txt", "UTF-8");
		for (Tuple2<Object, Vector> tuple : td) {
			writer.print("Id dokumentu: " + tuple._1());
			writer.print(" Wagi kolejnych topicow: ");

			double[] wektor = tuple._2().toArray();
			for (int i = 0; i < wektor.length; i++)
				writer.print(wektor[i] + " ");
			writer.println();
		}
		writer.close();
	}
}
