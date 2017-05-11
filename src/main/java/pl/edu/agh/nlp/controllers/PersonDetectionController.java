package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.nlp.exceptions.NotFoundException;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.services.ArticlesService;
import pl.edu.agh.nlp.ner.PersonDetector;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonDetectionController {

	@NonNull
	private final ArticlesService articlesService;
	@NonNull
	private final PersonDetector personDetector;
	@NonNull
	private final AsyncController asyncController;

	@RequestMapping(value = "/ner/{articleId}")
	public ResponseEntity<List<String>> findPersons(@PathVariable Integer articleId) throws NotFoundException {
		Article article = articlesService.findByIdOrThrow(articleId);
		return new ResponseEntity<>(personDetector.detect(article.getText()), OK);
	}

	@RequestMapping(value = "/ner", method = POST)
	public ResponseEntity<List<String>> findPersons(@RequestBody Article article) {
		return new ResponseEntity<>(personDetector.detect(article.getText()), OK);
	}

	@RequestMapping(value = "/ner/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildNERModelAsync();
		return new ResponseEntity<>(OK);
	}

}
