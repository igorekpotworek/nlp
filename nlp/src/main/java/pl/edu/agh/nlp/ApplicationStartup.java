package pl.edu.agh.nlp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.edu.agh.nlp.elasticsearch.ElasticsearchSessionManager;

@Component
public class ApplicationStartup implements InitializingBean {

	private static final Logger logger = Logger.getLogger(ApplicationStartup.class);

	@Autowired
	private ElasticsearchSessionManager elasticsearchSessionManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("No suitable driver", e);
		}
		elasticsearchSessionManager.createSession();
	}
}
