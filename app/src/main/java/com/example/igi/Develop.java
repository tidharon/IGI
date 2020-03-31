package com.example.igi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Develop extends AppCompatActivity implements View.OnClickListener {
Button butPhotoDvlp;
Button butRecDvlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);

        butPhotoDvlp = (Button)findViewById(R.id.butPhotoDvlp);
        butPhotoDvlp.setOnClickListener(this);
        butRecDvlp = (Button)findViewById(R.id.butRecDvlp);
        butRecDvlp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
