package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {
    Button butToDiscover;
    Button butToSolution;
    Button butToRate;
    Button butToDevelop;
    ImageView butInfo;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        butToDiscover = (Button) findViewById(R.id.butToDiscover);
        butToDiscover.setOnClickListener(this);
        butToSolution = (Button) findViewById(R.id.butToSolution);
        butToSolution.setOnClickListener(this);
        butToRate = (Button) findViewById(R.id.butToRate);
        butToRate.setOnClickListener(this);
        butToDevelop = (Button) findViewById(R.id.butToDevelop);
        butToDevelop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == butToDiscover) {
            Intent intentDiscover = new Intent(this, Problem.class);
            startActivity(intentDiscover);
        }

        if (v == butToSolution) {
            Intent intentSolution = new Intent(this, Solution.class);
            startActivity(intentSolution);
        }
        if (v == butToRate) {
            Intent intentRate = new Intent(this, Rate.class);
            startActivity(intentRate);
        }
        if (v == butToDevelop) {
            Intent intentDevelop = new Intent(this, Develop.class);
            startActivity(intentDevelop);
        }
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
    }
}
