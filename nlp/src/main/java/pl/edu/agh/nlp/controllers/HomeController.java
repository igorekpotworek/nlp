package pl.edu.agh.nlp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	// private static final Logger logger = LoggerFactory
	// .getLogger(HomeController.class);

	@RequestMapping(value = "/")
	public String home() {
		return "Hello in NLP System";
	}

}
