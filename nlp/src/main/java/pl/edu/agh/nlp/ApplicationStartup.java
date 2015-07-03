package pl.edu.agh.nlp;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = Logger.getLogger(ApplicationStartup.class);

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("No suitable driver", e);
		}
	}
}
