package com.example.igi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class Rate extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
RadioGroup butRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
butRate = (RadioGroup)findViewById(R.id.butRate);
butRate.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
