package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button butLogin;
Button butToSignUp;
TextView mainTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butLogin=(Button)findViewById(R.id.ButLogin);
        butToSignUp=(Button)findViewById(R.id.ButToSignUp);

        butLogin.setOnClickListener(this);
        butToSignUp.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == butToSignUp) {
                Intent intentLogin = new Intent(this, signup.class);
                startActivity(intentLogin);
                finish();
            }
        }
    }

