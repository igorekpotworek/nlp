package pl.edu.agh.nlp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> addUser(@RequestBody User user) {
		System.out.println(user);
		usersDao.insert(user);
		return new ResponseEntity<String>("ok", HttpStatus.OK);

	}

	@RequestMapping(value = "/add/rating", method = RequestMethod.POST)
	public ResponseEntity<String> addRating(@RequestBody Rate rate) {
		ratesDao.insert(rate);
		return new ResponseEntity<String>("ok", HttpStatus.OK);

	}
}
