package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class signup extends AppCompatActivity implements OnClickListener{
    Button butSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        butSignUp = (Button)findViewById(R.id.ButSingUp);
        butSignUp.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v==butSignUp)
        {
            Intent intentHomeScreen = new Intent(this, HomeScreen.class);
            startActivity(intentHomeScreen);
            finish();
        }
     }
}
