package com.example.igi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.igi.R.*;

public class Develop extends AppCompatActivity implements View.OnClickListener {
    Button butSubmit;
    ImageView butInfo;
    EditText frmTitle, frmDes;
    FileOutputStream devFile;
    String titleText, desText, allText;
    int num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_develop);
        butSubmit = (Button) findViewById(R.id.dvlpSubmitBut);
        butSubmit.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        frmTitle = findViewById(id.frmDvlpTitle);
        frmDes = findViewById(id.frmDvlpDes);
        frmTitle.setOnClickListener(this);
        frmDes.setOnClickListener(this);
        num++;
    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            saveTextDev();
            Toast.makeText(getApplicationContext(), "Thank You For Helping!", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(this, HomeScreen.class);
            startActivity(intentLogin);
            finish();
        }

    }

    public void saveTextDev() {
        titleText = (frmTitle.getText().toString() + "/n/n");
        desText = frmDes.getText().toString();
        allText = (titleText + desText);
        try {
            devFile = openFileOutput(("developer " + num), MODE_PRIVATE);
            devFile.write(allText.getBytes());
            devFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
