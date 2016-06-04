package com.sineong.testopenweatherapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sineong.testopenweatherapi.Client.OpenWeatherAPIClient;
import com.sineong.testopenweatherapi.Client.Weather;

public class ShowWeatherInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather_info);


        TextView temp = (TextView)findViewById(R.id.temperature);

        getWeather(temp);

    }

    public void getWeather(View view) {


        // 날씨를 읽어오는 API 호출

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();
                double lat = intent.getDoubleExtra("LATITUDE", 0);
                double lon = intent.getDoubleExtra("LONGITUDE", 0);

                OpenWeatherAPIClient call = new OpenWeatherAPIClient();
                final Weather w = call.getWeather(lat, lon);
                System.out.println("Temp :" + w.getTemprature());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView temp = (TextView) findViewById(R.id.temperature);
                        TextView min_temp = (TextView) findViewById(R.id.min_temp);
                        TextView max_temp = (TextView) findViewById(R.id.max_temp);
                        TextView windspeed = (TextView) findViewById(R.id.windspeed);
       //                 TextView description = (TextView) findViewById(R.id.description);

                        String t = String.valueOf(w.getTemprature());
                        String min_t = String.valueOf(w.getMin_temp());
                        String max_t = String.valueOf(w.getMax_temp());
                        String wind = String.valueOf(w.getWind_speed());
       //                 String desc = String.valueOf(w.getDescription());

                        temp.setText(t);
                        min_temp.setText(min_t);
                        max_temp.setText(max_t);
                        windspeed.setText(wind);
        //                description.setText(desc);
                    }
                });
            }
        });
        t.start();

    }
}
