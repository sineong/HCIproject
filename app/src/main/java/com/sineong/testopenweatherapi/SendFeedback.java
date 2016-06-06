package com.sineong.testopenweatherapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sineong.testopenweatherapi.DB.Criteria;
import com.sineong.testopenweatherapi.DB.MyDBHandler;

public class SendFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        Intent intent = getIntent();
        ((TextView) findViewById(R.id.date)).setText(intent.getIntExtra("month",0) + "월 " + intent.getIntExtra("day",0) + "일");
        ((TextView) findViewById(R.id.temperature)).setText(intent.getIntExtra("temp",0)+"℃");
        ((TextView) findViewById(R.id.daily)).setText(intent.getIntExtra("daily_max",0)+" / "+intent.getIntExtra("daily_min",0));

        ImageView topView = (ImageView) findViewById(R.id.top);
        ImageView bottomView = (ImageView) findViewById(R.id.bottom);


        if(intent.getIntExtra("daily_max",0) >= intent.getIntExtra("inner_max",0))
            topView.setImageResource(R.drawable.top_short);

        else
            topView.setImageResource(R.drawable.top_long);

        if(intent.getIntExtra("daily_max",0) >= intent.getIntExtra("bottom_max",0))
            bottomView.setImageResource(R.drawable.bottom_short);
        else
            bottomView.setImageResource(R.drawable.bottom_long);

    }

    public void sendFeedback(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Criteria new_criteria = new Criteria();
        Criteria old_criteria = dbHandler.findLatestCriteria();
        int new_inner_max = old_criteria.getInner_max();
        int new_bottom_max = old_criteria.getBottom_max();

        CheckBox top_cold = (CheckBox) findViewById(R.id.top_cold);
        CheckBox bottom_cold = (CheckBox) findViewById(R.id.bottom_cold);
        CheckBox top_hot = (CheckBox) findViewById(R.id.top_hot);
        CheckBox bottom_hot = (CheckBox) findViewById(R.id.bottom_hot);

        if(top_cold.isChecked())
            new_inner_max += 2;
        if(bottom_cold.isChecked())
            new_bottom_max += 2;
        if(top_hot.isChecked())
            new_inner_max -= 2;
        if(bottom_hot.isChecked())
            new_bottom_max -= 2;

        new_criteria.setInner_max(new_inner_max);
        new_criteria.setBottom_max(new_bottom_max);
        new_criteria.setOuter_min1(old_criteria.getOuter_min1());
        new_criteria.setOuter_min2(old_criteria.getOuter_min2());

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
