package com.example.chaoticsubway;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CongestionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congestion);

        TextView depStation = (TextView)findViewById(R.id.depStation);
        TextView curStation = (TextView)findViewById(R.id.curStation);
        TextView arrStation = (TextView)findViewById(R.id.arrStation);

    }
}
