package com.example.igi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {
    /*
    Double back press to exit app
     */
    private static long back_pressed;
    private Button butToDiscover;
    private Button butToSolution;
    private Button butToDevelop;
    private ImageView butInfo;
    private ImageView butLogout;
    private ProgressBar PBLogout;

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
        butToDevelop = (Button) findViewById(R.id.butToDevelop);
        butToDevelop.setOnClickListener(this);
        butLogout = (ImageView) findViewById(R.id.logout_ic);
        butLogout.setOnClickListener(this);
        PBLogout = findViewById(R.id.logout_progressBar);
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

        if (v == butToDevelop) {
            Intent intentDevelop = new Intent(this, Develop.class);
            startActivity(intentDevelop);
        }
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butLogout) {
            PBLogout.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
