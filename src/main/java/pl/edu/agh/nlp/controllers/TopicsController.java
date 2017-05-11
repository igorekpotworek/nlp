package pl.edu.agh.nlp.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.nlp.model.entities.Topic;
import pl.edu.agh.nlp.model.entities.TopicArticle;
import pl.edu.agh.nlp.model.services.TopicsService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TopicsController {

	@NonNull
	private final TopicsService topicsService;
	@NonNull
	private final AsyncController asyncController;

	@RequestMapping(value = "/topics/article/{articleId}")
	public ResponseEntity<List<TopicArticle>> getTopicsOfArticle(@PathVariable Integer articleId) {
		return new ResponseEntity<>(topicsService.findByTopicsByArticleId(articleId), OK);
	}

	@RequestMapping(value = "/topics")
	public ResponseEntity<List<Topic>> getAll() {
		return new ResponseEntity<>(topicsService.findAll(), OK);
	}

	@RequestMapping(value = "/topics/rebuild")
	public ResponseEntity<String> rebuildModel() {
		asyncController.buildLDAModelAsync();
		return new ResponseEntity<>(OK);
	}

}
