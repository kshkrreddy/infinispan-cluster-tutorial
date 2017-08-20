package com.weather.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.infinispan.Cache;

import infinispan.tutorial.embedded.CachingWeatherService;

public class WeatherServlet extends HttpServlet
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static String STANDALONE_APP = "1";
    public String WEB_APP = "2";

    private static final Logger log = Logger.getLogger(WeatherServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("doGet");

        String location = request.getParameter("location_name");
        String weather = "";
        CachingWeatherService app = new CachingWeatherService(WEB_APP);

        app.loadCacheData();

        Cache<String, String> cache = app.getCache();

        for (Map.Entry entry : cache.entrySet())
        {
            log.debug(entry.getKey() + " --> " + entry.getValue());
        }

        weather = app.getWeatherForLocation(location);

        log.debug("Got the location " + location + " Weather : " + weather);

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "WeatherData";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n"
                + "<body bgcolor = \"#f0f0f0\">\n" + "<h1 align = \"center\">" + title + "</h1>\n" + "<ul>\n" + location
                + " :   " + "  <li><b>Last Name</b>: " + weather + "\n" + "</ul>\n" + "</body></html>");
    }

    // Method to handle POST method request.
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("doPost");

        doGet(request, response);
    }

}