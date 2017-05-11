package pl.edu.agh.nlp.model.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TopicArticle {

	private Integer articleId;
	private Integer topicId;
	private Double weight;
}
