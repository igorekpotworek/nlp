package pl.edu.agh.nlp.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchSessionManager {

	private Client client;

	@SuppressWarnings("resource")
	public void createSession() {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
	}

	public Client getClient() {
		if (client == null)
			createSession();
		return client;
	}

	public void closeSession() {
		client.close();
	}
}
