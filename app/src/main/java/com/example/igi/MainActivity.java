package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button butLogin;
Button butToSignUp;
Button butInfo;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butLogin=(Button)findViewById(R.id.ButLogin);
        butToSignUp=(Button)findViewById(R.id.ButToSignUp);
butInfo = (Button)findViewById(R.id.info_but);
        butLogin.setOnClickListener(this);
        butToSignUp.setOnClickListener(this);
        butInfo.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == butToSignUp) {
                Intent intentLogin = new Intent(this, signup.class);
                startActivity(intentLogin);
                finish();
            }
            if (v==butInfo) {
                Intent intentDiscover = new Intent(this, InfoPage.class);
                startActivity(intentDiscover);
                finish();
            }
        }
    }

