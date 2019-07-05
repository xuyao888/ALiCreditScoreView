package com.robot.zhenai.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ALiStepScoreView aliStepView;
    private  ALiNewWaterView alinewWaterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aliStepView = findViewById(R.id.aliStepView);
        aliStepView.setScore(632);
        alinewWaterView = findViewById(R.id.alinewWaterView);
        alinewWaterView.setScore(780);
    }
}
