package com.sineong.testopenweatherapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import com.sineong.testopenweatherapi.Client.OpenWeatherAPIClient;
import com.sineong.testopenweatherapi.Client.Weather;
import com.sineong.testopenweatherapi.DB.Criteria;
import com.sineong.testopenweatherapi.DB.MyDBHandler;

public class ShowWeatherInfo extends AppCompatActivity {

    Calendar cal;
    int month;
    int day;
    int currentTemp;
    int inner_max;
    int bottom_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather_info);

        cal = Calendar.getInstance();
        setDate();

        TextView temp = (TextView)findViewById(R.id.temperature);
        getCriteria(temp);
        getWeather(temp);

    }
    private void setDate() {
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        ((TextView) findViewById(R.id.date)).setText(month + "월 " + day + "일");
    }

    public void getCriteria(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Criteria criteria = dbHandler.findLatestCriteria();
        //db test
        TextView testView = (TextView) findViewById(R.id.dbtest);
        testView.setText(criteria.getInner_max()+" / "+criteria.getBottom_max());
        //
        inner_max = criteria.getInner_max();
        bottom_max = criteria.getBottom_max();
    }

    public void feedback(View view) {

        Intent intent = new Intent(this, SendFeedback.class);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("temp", currentTemp);
        intent.putExtra("inner_max", inner_max);
        intent.putExtra("bottom_max", bottom_max);
        startActivity(intent);
    }

    public void getWeather(View view) {

        final Bitmap[] bitmap = new Bitmap[1];

        // 날씨를 읽어오는 API 호출

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();
                double lat = intent.getDoubleExtra("LATITUDE", 0);
                double lon = intent.getDoubleExtra("LONGITUDE", 0);


                OpenWeatherAPIClient call = new OpenWeatherAPIClient();
                final Weather w = call.getWeather(lat, lon);
                currentTemp = (int)w.getTemprature();
                //System.out.println("Temp :" + w.getTemprature());
                String iconURL = "http://openweathermap.org/img/w/"+String.valueOf(w.getIcon())+".png";

                try {
                    URL url = new URL(iconURL);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView temp = (TextView) findViewById(R.id.temperature);
                        //                TextView min_temp = (TextView) findViewById(R.id.min_temp);
                        //              TextView max_temp = (TextView) findViewById(R.id.max_temp);
                        //            TextView windspeed = (TextView) findViewById(R.id.windspeed);
                        //                 TextView description = (TextView) findViewById(R.id.description);

                        String t = String.valueOf((int) w.getTemprature()) + "℃";
                        String min_t = String.valueOf(w.getMin_temp());
                        String max_t = String.valueOf(w.getMax_temp());
                        String wind = String.valueOf(w.getWind_speed());
                        String desc = String.valueOf(w.getDescription());


                        temp.setText(t);
                        //                min_temp.setText(min_t);
                        //              max_temp.setText(max_t);
                        //            windspeed.setText(wind);
                        //                description.setText(desc);

                        ImageView myIcon = (ImageView) findViewById(R.id.icon);
                        myIcon.setImageBitmap(bitmap[0]);

                        ImageView topView = (ImageView) findViewById(R.id.top);
                        ImageView bottomView = (ImageView) findViewById(R.id.bottom);
                        ImageView umbrella = (ImageView) findViewById(R.id.umbrella);
                        TextView isRaing = (TextView) findViewById(R.id.isRaining);
                        isRaing.setText(w.getDescription());

                        getCriteria(umbrella);

                        if (w.getTemprature() >= inner_max)
                            topView.setImageResource(R.drawable.top_short);

                        else
                            topView.setImageResource(R.drawable.top_long);

                        if (w.getTemprature() >= bottom_max)
                            bottomView.setImageResource(R.drawable.bottom_short);
                        else
                            bottomView.setImageResource(R.drawable.bottom_long);

                        if (w.getDescription().contains("rain")) {
                            umbrella.setImageResource(R.drawable.umbrella);
                        }
                    }
                });
            }
        });
        t.start();

    }
}
