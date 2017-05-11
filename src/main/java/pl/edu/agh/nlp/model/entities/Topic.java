package pl.edu.agh.nlp.model.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Topic {

	private  Integer topicId;
	private  String word;
	private  Double weight;

}
