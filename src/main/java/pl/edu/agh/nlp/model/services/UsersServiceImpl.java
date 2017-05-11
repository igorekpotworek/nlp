package pl.edu.agh.nlp.model.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.User;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UsersServiceImpl implements UsersService {

	@NonNull
	private final UsersDao usersDao;

	@Override
	public void save(final User user) {
		usersDao.save(user);
	}
}
