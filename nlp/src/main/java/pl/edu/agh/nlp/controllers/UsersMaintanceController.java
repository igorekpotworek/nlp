package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.User;

@RestController
public class UsersMaintanceController {
	@Autowired
	private UsersDao usersDao;

	@RequestMapping(value = "/add/user", method = RequestMethod.POST)
	public void addArticle(@RequestBody User user) {
		usersDao.insert(user);
	}
}
