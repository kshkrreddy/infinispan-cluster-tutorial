package infinispan.tutorial.embedded;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.logging.Logger;

public class WeatherApp {

	private static final Logger log = Logger.getLogger(WeatherApp.class);

	final Random random = new Random();
	private static volatile WeatherApp instance;
	private static String urlPkgPrefixes = "org.jboss.naming:org.jnp.interfaces";

	public String STANDALONE_APP = "1";
	public String STANDALONE_APP_CACHE_XML_FILE_NAME = "cache.xml";
	public String WEB_APP_JNDI_NAME = "java:jboss/infinispan/web/tutorial";
	public String WEB_APP = "2";

	private EmbeddedCacheManager cacheManager;

	public static WeatherApp getInstance(String appType) {
		if (instance == null) {
			synchronized (WeatherApp.class) {
				log.info("INTIALIZING AS THE FIRST TIME");
				instance = new WeatherApp(appType);
			}
		}

		return instance;
	}

	private WeatherApp(String appType) {
		try {

			if (STANDALONE_APP.equals(appType)) {
				cacheManager = new DefaultCacheManager(STANDALONE_APP_CACHE_XML_FILE_NAME, true);
			} else {
				Properties props = new Properties();
				props.setProperty(Context.URL_PKG_PREFIXES, urlPkgPrefixes);
				InitialContext ic = new InitialContext(props);
				cacheManager = (EmbeddedCacheManager) (ic.lookup(WEB_APP_JNDI_NAME));
			}

			// GlobalConfigurationBuilder global =
			// GlobalConfigurationBuilder.defaultClusteredBuilder();
			// global.transport().clusterName("WeatherApp");
			// cacheManager = new DefaultCacheManager(global.build());
			// ConfigurationBuilder config = new ConfigurationBuilder();
			// config.expiration().lifespan(100,
			// TimeUnit.SECONDS).clustering().cacheMode(CacheMode.DIST_SYNC);
			// cacheManager.defineConfiguration("weather", config.build());

		} catch (Exception e) {
			log.error("Error Occured in Initlizing the cacheManager"+e);
		}

	}

	public void shutdown() {
		cacheManager.stop();
	}

	public EmbeddedCacheManager getCacheManager() {
		return cacheManager;
	}

}
