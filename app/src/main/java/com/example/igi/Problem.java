package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Problem extends AppCompatActivity implements View.OnClickListener {
Button butPhotoPrblm;
Button butRecPrblm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        butPhotoPrblm = (Button)findViewById(R.id.butPhotoPrblm);
        butPhotoPrblm.setOnClickListener(this);
        butRecPrblm = (Button)findViewById(R.id.butRecPrblm);
        butRecPrblm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
