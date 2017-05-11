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
import pl.edu.agh.nlp.model.entities.Article.Category;
import pl.edu.agh.nlp.model.services.ArticlesService;
import pl.edu.agh.nlp.spark.algorithms.classification.ClassificationService;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassificationController {

	@NonNull
	private final ArticlesService articlesService;
	@NonNull
	private final ClassificationService classificationService;
	@NonNull
	private final AsyncController asyncController;

	@RequestMapping(value = "/classify/{articleId}")
	public ResponseEntity<Category> classifyArticleById(@PathVariable Integer articleId) throws NotFoundException {
		Article article = articlesService.findByIdOrThrow(articleId);
		return classifyArticle(article);
	}

	@RequestMapping(value = "/classify", method = POST)
	public ResponseEntity<Category> classifyArticle(@RequestBody Article article) {
		if (!classificationService.isPresent())
			classificationService.buildModel();
		return new ResponseEntity<>(classificationService.predictCategory(article.getText()), OK);
	}

	@RequestMapping(value = "/classify/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildClassificationModelAsync();
		return new ResponseEntity<>(OK);
	}
}
