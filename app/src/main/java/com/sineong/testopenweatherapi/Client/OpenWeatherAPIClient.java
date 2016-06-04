package com.sineong.testopenweatherapi.Client;

/**
 * Created by sineong on 2016. 6. 2..
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenWeatherAPIClient {

    final static String openWeatherURL = "http://api.openweathermap.org/data/2.5/weather";

    public Weather getWeather(double lat,double lon) {
        Weather w;
        String urlString = openWeatherURL + "?lat=" + lat + "&lon=" + lon + "&APPID=f5e8344b7c3fc41944364becab4cb7fe";

        try {
            // call API by using HTTPURLConnection
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = new JSONObject(getStringFromInputStream(in));

            // parse JSON
            w = parseJSON(json);
            w.setLon(lon);
            w.setLat(lat);


        }catch(MalformedURLException e){
            System.err.println("Malformed URL");
            e.printStackTrace();
            return null;

        }catch(JSONException e) {
            System.err.println("JSON parsing error");
            e.printStackTrace();
            return null;
        }catch(IOException e){
            System.err.println("URL Connection failed");
            e.printStackTrace();
            return null;
        }

        // set Weather Object

        return w;
    }

    private Weather parseJSON(JSONObject json) throws JSONException {
        Weather w = new Weather();
//        w.setDescription(json.getString("description"));
        w.setTemprature(json.getJSONObject("main").getDouble("temp"));
        w.setMin_temp(json.getJSONObject("main").getDouble("temp_min"));
        w.setMax_temp(json.getJSONObject("main").getDouble("temp_max"));
        w.setCity(json.getString("name"));
        w.setWind_speed(json.getJSONObject("wind").getDouble("speed"));
//        w.setCloudy(json.getInt("clouds"));

        return w;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
