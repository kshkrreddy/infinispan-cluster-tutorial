package infinispan.tutorial.embedded;

import java.util.Map;
import java.util.Random;

import org.infinispan.Cache;
import org.infinispan.lifecycle.ComponentStatus;
import org.jboss.logging.Logger;

public class CachingWeatherService implements WeatherService {

	private static final Logger log = Logger.getLogger(CachingWeatherService.class);

	final Random random = new Random();

	private Cache<String, String> cache;
	String appType = "";

	static final String[] locations = { "Rome", "Como", "Basel", "Bern", "London", "Newcastle", "Bucarest", "Ottawa",
			"Toronto", "Lisbon", "Porto", "Raleigh", "Washington" };

	public CachingWeatherService(String appType) {

		log.info("CachingWeatherService");

		this.appType = appType;
		cache = WeatherApp.getInstance(appType).getCacheManager().getCache("weather");
	}

	final public String getWeatherForLocation(String location) {

		log.debug("getWeatherForLocation" + cache);

		String weather = cache.get(location);

		log.debug("Got weather from the cache : " + weather);

		if (weather == null) {
			log.debug("Got weather from the cache null ,So generating and adding to the cache : " + weather);

			weather = location + "-" + random.nextInt();
			cache.put(location, weather);
		}

		log.debug("All the Entries from the Cache -----------");
		for (Map.Entry entry : cache.entrySet()) {
			log.debug(entry.getKey() + " --> " + entry.getValue());
		}

		return weather;
	}

	final public void updateWeatherForLocation(String location, String weather) {

		log.debug("updateWeatherForLocation");

		log.info("Before Updation LOCATION : " + location + " Weather : " + cache.get(location));

		cache.put(location, weather);

		log.info("AFTER Updation LOCATION : " + location + " Weather : " + cache.get(location));

	}

	public void loadCacheData() {
		log.info("loadCacheData");

		while (true) {
			if (ComponentStatus.RUNNING.equals(cache.getStatus())) {
				log.info(" Already Runninng Cache " + cache.getName() + "Status : " + cache.getStatus());
				break;
			} else {
				log.debug("Cache " + cache.getName() + "Status : " + cache.getStatus());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error("Error while checking the cache status :" + e);

				}
			}
		}
		if (cache.isEmpty()) {
			log.info("Loading the Data in Cache as it is empty");
			cache.put("TESTENTRY", "TESTENTRY");
			String weather = "";
			for (String location : locations) {
				weather = location + "-" + random.nextInt();

				cache.put(location, weather);

				log.debug("Location " + location + "Weather : " + weather);

			}
		} else {
			log.info("Already Data Present");
			log.info("Data1 : " + cache);
			for (Map.Entry entry : cache.entrySet()) {
				log.debug(entry.getKey() + " --> " + entry.getValue());
			}
		}

	}

	public void addWeatherForLocation(String location, String weather) {
		log.info("addWeatherForLocation");

		String addEntry = cache.put(location, weather);

		log.info("Added Entry in cache :" + addEntry);

	}

	public void deleteWeatherForLocation(String location) {
		log.info("deleteWeatherForLocation");

		String removeEntry = cache.remove(location);

		log.info("Removed Entry from cache :" + removeEntry);
	}

	public Cache<String, String> getCache() {
		return cache;
	}

	public void setCache(Cache<String, String> cache) {
		this.cache = cache;
	}

}
