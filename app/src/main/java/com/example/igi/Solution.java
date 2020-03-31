package com.example.igi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Solution extends AppCompatActivity implements View.OnClickListener {
Button butPhotoSltn;
Button butRecSltn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        butPhotoSltn = (Button)findViewById(R.id.butPhotoSltn);
        butPhotoSltn.setOnClickListener(this);
        butRecSltn = (Button)findViewById(R.id.butRecSltn);
        butRecSltn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
