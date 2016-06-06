package com.sineong.testopenweatherapi.Client;

/**
 * Created by sineong on 2016. 6. 2..
 */
public class Weather {

    double lat;
    double lon;
    String description;
    double temperature;
    double min_temp;
    double max_temp;
    double wind_speed;
    int humidity;
    int cloudy;
    String city;
    String icon;

    public void setLat(double lat){ this.lat = lat;}
    public void setLon(double lon){ this.lon = lon;}
    public void setDescription(String description) { this.description = description;}
    public void setTemprature(double t){ this.temperature = t;}
    public void setMin_temp(double min_temp) { this.min_temp = min_temp;}
    public void setMax_temp(double max_temp) { this.max_temp = max_temp;}
    public void setWind_speed(double wind_speed) { this.wind_speed = wind_speed;}
    public void setHumidity(int humidity) { this.humidity = humidity;}
    public void setCloudy(int cloudy){ this.cloudy = cloudy;}
    public void setCity(String city){ this.city = city;}
    public void setIcon(String icon){ this.icon = icon;}

    public double getLat(){ return lat;}
    public double getLon() { return lon;}
    public String getDescription() { return description;}
    public double getTemprature() { return temperature - 273;}
    public double getMin_temp() { return min_temp;}
    public double getMax_temp() { return max_temp;}
    public double getWind_speed() { return wind_speed;}
    public int getHumidity() { return humidity;}
    public int getCloudy() { return cloudy;}
    public String getCity() { return city;}
    public String getIcon() { return icon;}
}
