package pl.edu.agh.nlp;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.nlp.elasticsearch.ElasticsearchSessionManager;

@Component
@Log4j
public class ApplicationStartup implements InitializingBean {

	@Autowired
	private ElasticsearchSessionManager elasticsearchSessionManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			log.error("No suitable driver", e);
		}
		elasticsearchSessionManager.createSession();
	}
}
