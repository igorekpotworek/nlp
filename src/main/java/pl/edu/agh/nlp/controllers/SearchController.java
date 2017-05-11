package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.nlp.dto.SearchBody;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.services.ArticlesService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchController {

	@NonNull
	private final ArticlesService articlesService;

	@RequestMapping(value = "/search", method = POST)
	public ResponseEntity<List<Article>> search(@RequestBody SearchBody body) {
		return new ResponseEntity<>(articlesService.findBySentence(body.getSentence()), OK);
	}

}
