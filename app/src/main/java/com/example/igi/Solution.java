package com.example.igi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Solution extends AppCompatActivity implements View.OnClickListener {
    Button butPhotoSltn;
    Button butRecSltn;
    ImageView butInfo;
    Button butSubmit;
    Spinner listProblem;
    ArrayAdapter<String> problemArray;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        butPhotoSltn = (Button) findViewById(R.id.butPhotoSltn);
        butPhotoSltn.setOnClickListener(this);
        butRecSltn = (Button) findViewById(R.id.butRecSltn);
        butRecSltn.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        butSubmit = (Button) findViewById(R.id.submit_but);
        butInfo.setOnClickListener(this);
        listProblem = (Spinner) findViewById(R.id.problemList);
        problemArray = new ArrayAdapter<>(Solution.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.problemList));
        problemArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listProblem.setAdapter(problemArray);
    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
    }
}
