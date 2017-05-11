package pl.edu.agh.nlp.spark.algorithms.lda;

import org.apache.spark.mllib.linalg.Vector;
import pl.edu.agh.nlp.model.entities.TopicArticle;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class TopicsDistributionWriter implements Function<List<Tuple2<Object, Vector>>, List<TopicArticle>> {

	@Override
	public List<TopicArticle> apply(List<Tuple2<Object, Vector>> td) {
		List<TopicArticle> topicArticles = new ArrayList<>();
		for (Tuple2<Object, Vector> tuple : td) {
			double[] vector = tuple._2().toArray();
			for (int i = 0; i < vector.length; i++)
				topicArticles.add(new TopicArticle(((Long) tuple._1()).intValue(), i, vector[i]));
		}
		return topicArticles;	}
}
