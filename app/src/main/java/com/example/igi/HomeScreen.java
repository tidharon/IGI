package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {
    Button butToDiscover;
    Button butToSolution;
    Button butToRate;
    Button butToDevelop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

butToDiscover = (Button)findViewById(R.id.butToDiscover);
butToDiscover.setOnClickListener(this);
butToSolution = (Button)findViewById(R.id.butToSolution);
butToSolution.setOnClickListener(this);
butToRate = (Button)findViewById(R.id.butToRate);
butToRate.setOnClickListener(this);
butToDevelop = (Button)findViewById(R.id.butToDevelop);
butToDevelop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==butToDiscover){
            Intent intentDiscover = new Intent(this, Problem.class);
            startActivity(intentDiscover);
            finish();
        }
        if (v==butToSolution){
            Intent intentSolution = new Intent(this, Solution.class);
            startActivity(intentSolution);
            finish();
        }
        if (v==butToRate){
            Intent intentRate = new Intent(this, Rate.class);
            startActivity(intentRate);
            finish();
        }
        if (v==butToDevelop){
            Intent intentDevelop = new Intent(this, Develop.class);
            startActivity(intentDevelop);
            finish();
        }
    }
}
