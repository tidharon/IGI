package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class signup extends AppCompatActivity implements OnClickListener{
    Button butSignUp;
ImageView butInfo;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        butSignUp = (Button)findViewById(R.id.ButSingUp);
        butSignUp.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v==butSignUp)
        {
            Intent intentHomeScreen = new Intent(this, HomeScreen.class);
            startActivity(intentHomeScreen);
            finish();
        }
        if (v==butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
            finish();
        }
     }
}
