package com.example.igi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.igi.R.*;

public class Develop extends AppCompatActivity implements View.OnClickListener {
Button butPhotoDvlp;
Button butRecDvlp;
ImageView butInfo;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_develop);

        butPhotoDvlp = (Button)findViewById(id.butPhotoDvlp);
        butPhotoDvlp.setOnClickListener(this);
        butRecDvlp = (Button)findViewById(id.butRecDvlp);
        butRecDvlp.setOnClickListener(this);
        butInfo = (ImageView) findViewById(id.info_but);
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
