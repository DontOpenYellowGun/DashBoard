package com.sven.dashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AirBoardView airview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        airview = (AirBoardView) findViewById(R.id.airView);
        airview.setOnAirClickListener(new AirBoardView.OnAirClickListener() {
            @Override
            public void onAirClick(String temp) {
                Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
