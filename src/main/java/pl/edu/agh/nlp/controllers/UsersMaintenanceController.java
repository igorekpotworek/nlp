package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.nlp.model.entities.Rate;
import pl.edu.agh.nlp.model.entities.User;
import pl.edu.agh.nlp.model.services.RatesService;
import pl.edu.agh.nlp.model.services.UsersService;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersMaintenanceController {

	@NonNull
	private final UsersService usersService;
	@NonNull
	private final RatesService ratesService;

	@RequestMapping(value = "/add/user", method = POST)
	public ResponseEntity<String> addUser(@RequestBody User user) {
		usersService.save(user);
		return new ResponseEntity<>(OK);

	}

	@RequestMapping(value = "/add/rating", method = POST)
	public ResponseEntity<String> addRating(@RequestBody Rate rate) {
		ratesService.save(rate);
		return new ResponseEntity<>(OK);

	}
}
