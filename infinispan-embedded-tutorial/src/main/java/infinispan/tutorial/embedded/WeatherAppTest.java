package infinispan.tutorial.embedded;

import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;

public class WeatherAppTest
{

    private static final Logger log = Logger.getLogger(WeatherAppTest.class);
	public static String STANDALONE_APP = "1";
	public String WEB_APP = "2";

    public static void main(String[] args) throws Exception
    {
        log.debug("Started ....");

        CachingWeatherService app = new CachingWeatherService(STANDALONE_APP);
        int i=0;

        while (true)
        {
        	app.loadCacheData();
            app.getWeatherForLocation("Rome");
            
            app.updateWeatherForLocation("Rome","Sharks"+i);

            TimeUnit.SECONDS.sleep(15);
           
            //app.deleteWeatherForLocation("Rome");
            
            TimeUnit.SECONDS.sleep(15);


            i++;
        }
    }

}
