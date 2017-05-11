package pl.edu.agh.nlp.spark.algorithms.classification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.nlp.exceptions.AbsentModelException;
import pl.edu.agh.nlp.model.entities.Article.Category;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClassificationServiceImpl implements ClassificationService {

	@NonNull
	private final ClassificationModelBuilder classificationModelBuilder;

	private ClassificationModel classificationModel;

	@Override
	public void buildModel() {
		classificationModel = classificationModelBuilder.buildModel();
	}

	@Override
	public boolean isPresent(){
		return classificationModel!=null;
	}

	@Override
	public Category predictCategory(String text) {
		return Optional.ofNullable(classificationModel)
				.map(m -> m.predictCategory(text))
				.orElseThrow(AbsentModelException::new);
	}

}
