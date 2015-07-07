package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.nlp.model.dao.RatesDao;
import pl.edu.agh.nlp.model.dao.UsersDao;
import pl.edu.agh.nlp.model.entities.Rate;
import pl.edu.agh.nlp.model.entities.User;

@RestController
public class UsersMaintanceController {
	@Autowired
	private UsersDao usersDao;

	@Autowired
	private RatesDao ratesDao;

	@RequestMapping(value = "/add/user", method = RequestMethod.POST)
	public void addUser(@RequestBody User user) {
		usersDao.insert(user);
	}

	@RequestMapping(value = "/add/rating", method = RequestMethod.POST)
	public void addRating(@RequestBody Rate rate) {
		ratesDao.insert(rate);
	}
}
