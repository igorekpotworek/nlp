package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.UsersArticlesDao;
import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.User;
import pl.edu.agh.nlp.model.entities.UserArticle;

@RestController
public class UsersMaintanceController {
	@Autowired
	private UsersDao usersDao;

	@Autowired
	private UsersArticlesDao usersArticlesDao;

	@RequestMapping(value = "/add/user", method = RequestMethod.POST)
	public void addUser(@RequestBody User user) {
		usersDao.insert(user);
	}

	@RequestMapping(value = "/rate", method = RequestMethod.POST)
	public void addUser(@RequestBody UserArticle userArticle) {
		usersArticlesDao.insert(userArticle);
	}
}
