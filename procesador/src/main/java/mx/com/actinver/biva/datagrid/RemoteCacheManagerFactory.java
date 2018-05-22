package mx.com.actinver.biva.datagrid;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class RemoteCacheManagerFactory {
	
	private ConfigurationBuilder configurationBuilder;
	
	public RemoteCacheManagerFactory(String host, Integer port) {
		configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.addServer().host(host).port(port);
	}
	
	public RemoteCacheManager createNewRemoteCacheManager() {
		return new RemoteCacheManager(this.configurationBuilder.build(),true);
	}
	
}