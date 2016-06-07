package com.sineong.testopenweatherapi;

import android.content.Intent;
import android.content.res.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.sineong.testopenweatherapi.DB.Criteria;
import com.sineong.testopenweatherapi.DB.MyDBHandler;

import org.w3c.dom.*;

public class SendFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        Intent intent = getIntent();

        ((TextView) findViewById(R.id.date)).setText(intent.getIntExtra("month", 0) + "월 " + intent.getIntExtra("day", 0) + "일");
        ((TextView) findViewById(R.id.temperature)).setText(intent.getIntExtra("temp", 0) + "℃");
        ((TextView) findViewById(R.id.max)).setText(intent.getIntExtra("daily_max", 0) + "");
        ((TextView) findViewById(R.id.min)).setText(intent.getIntExtra("daily_min", 0) + "");

        ImageView topView = (ImageView) findViewById(R.id.top);
        ImageView bottomView = (ImageView) findViewById(R.id.bottom);

        ImageButton top_left_btn = (ImageButton) findViewById(R.id.top_left_btn);
        ImageButton top_right_btn = (ImageButton) findViewById(R.id.top_right_btn);
        ImageButton bottom_left_btn = (ImageButton) findViewById(R.id.bottom_left_btn);
        ImageButton bottom_right_btn = (ImageButton) findViewById(R.id.bottom_right_btn);

        TextView top_flag = (TextView) findViewById(R.id.top_flag);
        TextView bottom_flag = (TextView) findViewById(R.id.bottom_flag);

        if (intent.getIntExtra("daily_max", 0) >= intent.getIntExtra("inner_max", 0)){
            topView.setImageResource(R.drawable.top_short);
            top_flag.setText("short");
        }
        else {
            topView.setImageResource(R.drawable.top_long);
            top_flag.setText("long");
        }

        if(intent.getIntExtra("daily_max",0) >= intent.getIntExtra("bottom_max",0)) {
            bottomView.setImageResource(R.drawable.bottom_short);
            bottom_flag.setText("short");
        }
        else {
            bottomView.setImageResource(R.drawable.bottom_long);
            bottom_flag.setText("long");
        }

        if (top_flag.getText().toString() == "short"){
            top_right_btn.setVisibility(View.VISIBLE);
        }
        else{
            top_left_btn.setVisibility(View.VISIBLE);
        }

        if (bottom_flag.getText().toString() == "short"){
            bottom_right_btn.setVisibility(View.VISIBLE);
        }
        else{
            bottom_left_btn.setVisibility(View.VISIBLE);
        }



    }

    public void topRightButton(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView topView = (ImageView) findViewById(R.id.top);

                ImageButton top_left_btn = (ImageButton) findViewById(R.id.top_left_btn);
                ImageButton top_right_btn = (ImageButton) findViewById(R.id.top_right_btn);

                TextView top_flag = (TextView) findViewById(R.id.top_flag);

                top_right_btn.setVisibility(View.INVISIBLE);
                top_left_btn.setVisibility(View.VISIBLE);

                topView.setImageResource(R.drawable.top_long);
                top_flag.setText("long");
            }
        });
    }

    public void topLeftButton(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView topView = (ImageView) findViewById(R.id.top);

                ImageButton top_left_btn = (ImageButton) findViewById(R.id.top_left_btn);
                ImageButton top_right_btn = (ImageButton) findViewById(R.id.top_right_btn);

                TextView top_flag = (TextView) findViewById(R.id.top_flag);

                top_right_btn.setVisibility(View.VISIBLE);
                top_left_btn.setVisibility(View.INVISIBLE);

                topView.setImageResource(R.drawable.top_short);
                top_flag.setText("short");
            }
        });
    }
    public void bottomRightButton(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView bottomView = (ImageView) findViewById(R.id.bottom);

                ImageButton bottom_left_btn = (ImageButton) findViewById(R.id.bottom_left_btn);
                ImageButton bottom_right_btn = (ImageButton) findViewById(R.id.bottom_right_btn);

                TextView bottom_flag = (TextView) findViewById(R.id.bottom_flag);

                bottom_right_btn.setVisibility(View.INVISIBLE);
                bottom_left_btn.setVisibility(View.VISIBLE);

                bottomView.setImageResource(R.drawable.bottom_long);
                bottom_flag.setText("long");
            }
        });
    }
    public void bottomLeftButton(View view){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView bottomView = (ImageView) findViewById(R.id.bottom);

                ImageButton bottom_left_btn = (ImageButton) findViewById(R.id.bottom_left_btn);
                ImageButton bottom_right_btn = (ImageButton) findViewById(R.id.bottom_right_btn);

                TextView bottom_flag = (TextView) findViewById(R.id.bottom_flag);

                bottom_left_btn.setVisibility(View.INVISIBLE);
                bottom_right_btn.setVisibility(View.VISIBLE);

                bottomView.setImageResource(R.drawable.bottom_short);
                bottom_flag.setText("short");
            }
        });
    }

    public void sendFeedback(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Criteria new_criteria = new Criteria();
        Criteria old_criteria = dbHandler.findLatestCriteria();

        int my_inner_max = old_criteria.getInner_max();
        int my_bottom_max = old_criteria.getBottom_max();

        TextView max = (TextView) findViewById(R.id.max);

        int daily_max = Integer.parseInt(max.getText().toString());


        String top_flag = ((TextView) findViewById(R.id.top_flag)).getText().toString();
        String bottom_flag = ((TextView) findViewById(R.id.bottom_flag)).getText().toString();

        if ((daily_max > my_inner_max) && (top_flag == "long"))
            my_inner_max = daily_max;

        else if ((daily_max <= my_inner_max) && (top_flag == "short"))
            my_inner_max = daily_max - 1;



        if ((daily_max > my_bottom_max) && (bottom_flag == "long"))
            my_bottom_max = daily_max;
        else if ((daily_max <= my_bottom_max) && (bottom_flag == "short"))
            my_bottom_max = daily_max - 1;


        new_criteria.setInner_max(my_inner_max);
        new_criteria.setBottom_max(my_bottom_max);

        dbHandler.addCriteria(new_criteria);

        Intent oldintent = getIntent();
        double lat = oldintent.getDoubleExtra("LATITUDE", 0);
        double lon = oldintent.getDoubleExtra("LONGITUDE", 0);

        Intent intent = new Intent(this, ShowWeatherInfo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("LATITUDE", lat);
        intent.putExtra("LONGITUDE", lon);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
