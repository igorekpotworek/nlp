package pl.edu.agh.nlp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorMessage {

	@JsonProperty("error")
	private String error;
	@JsonProperty("error_description")
	private String errorDescription;

	public ErrorMessage(Throwable ex, String errorDescription) {
		super();
		error = ex.getClass().getSimpleName();
		this.errorDescription = errorDescription;
	}
}

