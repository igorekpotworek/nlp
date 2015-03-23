package pl.edu.agh.nlp;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {

	}
}
