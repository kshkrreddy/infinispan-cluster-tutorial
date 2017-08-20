package infinispan.tutorial.embedded;

public interface WeatherService {
	String getWeatherForLocation(String location);

	void updateWeatherForLocation(String location, String weather);

	void loadCacheData();

	public void addWeatherForLocation(String location, String weather);

	public void deleteWeatherForLocation(String location);

}
