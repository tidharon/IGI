package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Problem extends AppCompatActivity implements View.OnClickListener {
Button butPhotoPrblm;
Button butRecPrblm;
Button butInfo;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        butPhotoPrblm = (Button)findViewById(R.id.butPhotoPrblm);
        butPhotoPrblm.setOnClickListener(this);
        butRecPrblm = (Button)findViewById(R.id.butRecPrblm);
        butRecPrblm.setOnClickListener(this);
butInfo = (Button)findViewById(R.id.info_but);
butInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
            finish();
        }
    }
}
