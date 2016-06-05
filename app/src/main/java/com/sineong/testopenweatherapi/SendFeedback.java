package com.sineong.testopenweatherapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SendFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        Intent intent = getIntent();
        ((TextView) findViewById(R.id.date)).setText(intent.getIntExtra("month",0) + "월 " + intent.getIntExtra("day",0) + "일");
        ((TextView) findViewById(R.id.temperature)).setText(intent.getIntExtra("temp",0)+"℃");

        ImageView topView = (ImageView) findViewById(R.id.top);
        ImageView bottomView = (ImageView) findViewById(R.id.bottom);


        if(intent.getIntExtra("temp",0) > 30)
        {
            topView.setImageResource(R.drawable.top_short);
            bottomView.setImageResource(R.drawable.bottom_short);
        }
        else
        {
            topView.setImageResource(R.drawable.top_long);
            bottomView.setImageResource(R.drawable.bottom_long);
        }
    }
}
