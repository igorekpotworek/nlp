package pl.edu.agh.nlp.spark.algorithms.lda;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import pl.edu.agh.nlp.model.entities.Topic;
import scala.Tuple2;

import com.google.common.collect.Multimap;

@Service
public class TopicsDescriptionWriter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4227120558232602407L;
	private static final Logger logger = Logger.getLogger(SparkLDA.class);

	public void writeToFile(Tuple2<int[], double[]>[] d, Multimap<Integer, String> mapping) {
		try {
			PrintWriter writer = new PrintWriter("TOPICS_DESCRIPTION.txt", "UTF-8");
			for (int i = 0; i < d.length; i++) {
				writer.println("TOPIC " + i + " : ");
				for (int j = 0; j < d[i]._1.length; j++)
					writer.println("slowa: " + mapping.get(d[i]._1[j]) + " waga : " + d[i]._2[j]);
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			logger.error("Error during saving topics description to file", e);
		}

	}

	public List<Topic> convertToTopic(Tuple2<int[], double[]>[] d, Multimap<Integer, String> mapping) {
		List<Topic> topicWords = new ArrayList<Topic>();
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i]._1.length; j++) {
				for (String word : mapping.get(d[i]._1[j]))
					topicWords.add(new Topic(i, word, d[i]._2[j]));
			}
		}
		return topicWords;
	}
}
