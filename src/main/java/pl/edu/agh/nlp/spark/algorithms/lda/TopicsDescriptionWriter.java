package pl.edu.agh.nlp.spark.algorithms.lda;

import com.google.common.collect.Multimap;
import pl.edu.agh.nlp.model.entities.Topic;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

class TopicsDescriptionWriter implements BiFunction<Tuple2<int[], double[]>[], Multimap<Integer, String>, List<Topic>> {

	@Override
	public List<Topic> apply(Tuple2<int[], double[]>[] d, Multimap<Integer, String> mapping) {
		List<Topic> topicWords = new ArrayList<>();
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[i]._1.length; j++) {
				for (String word : mapping.get(d[i]._1[j]))
					topicWords.add(new Topic(i, word, d[i]._2[j]));
			}
		}
		return topicWords;
	}
}
