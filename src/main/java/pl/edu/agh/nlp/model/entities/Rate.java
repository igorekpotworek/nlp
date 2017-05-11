package pl.edu.agh.nlp.model.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Rate {

	private Integer userId;
	private Integer articleId;
	private Double rating;
}
