package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import pl.edu.agh.nlp.model.entities.Rate;

public interface RatesService {
	void save(@NonNull Rate rate);
}
