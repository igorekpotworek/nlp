package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.model.dao.RatesDao;
import pl.edu.agh.nlp.model.entities.Rate;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RatesServiceImpl implements RatesService {

	@NonNull
	private final RatesDao ratesDao;

	@Override
	public void save(@NonNull final Rate rate) {
		ratesDao.save(rate);
	}
}
