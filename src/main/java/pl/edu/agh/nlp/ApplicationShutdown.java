package pl.edu.agh.nlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import pl.edu.agh.nlp.elasticsearch.ElasticsearchSessionManager;

@Component
public class ApplicationShutdown implements ApplicationListener<ContextClosedEvent> {

	@Autowired
	private ElasticsearchSessionManager elasticsearchSessionManager;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if (elasticsearchSessionManager.getClient() != null)
			elasticsearchSessionManager.closeSession();
	}

}