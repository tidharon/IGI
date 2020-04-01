package com.example.igi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Solution extends AppCompatActivity implements View.OnClickListener {
Button butPhotoSltn;
Button butRecSltn;
Button butInfo;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        butPhotoSltn = (Button)findViewById(R.id.butPhotoSltn);
        butPhotoSltn.setOnClickListener(this);
        butRecSltn = (Button)findViewById(R.id.butRecSltn);
        butRecSltn.setOnClickListener(this);
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
