package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.nlp.model.entities.Article;
import pl.edu.agh.nlp.model.services.ArticlesService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticlesMaintenanceController {

	@NonNull
	private final ArticlesService articlesService;

	@RequestMapping(value = "/add/article", method = RequestMethod.POST)
	public ResponseEntity<String> addArticle(@RequestBody Article article) {
		articlesService.save(article);
		return new ResponseEntity<>(OK);
	}

	@RequestMapping(value = "/find/{articleId}")
	public ResponseEntity<Article> findById(@PathVariable Integer articleId) {
		return new ResponseEntity<>(articlesService.findById(articleId), OK);

	}
}
