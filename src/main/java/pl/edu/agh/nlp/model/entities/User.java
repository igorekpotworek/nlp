package pl.edu.agh.nlp.model.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {

	private Integer id;
	private String firstname;
	private String lastname;

}
