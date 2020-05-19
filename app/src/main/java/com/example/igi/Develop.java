package com.example.igi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.igi.R.id;
import static com.example.igi.R.layout;

public class Develop extends AppCompatActivity implements View.OnClickListener {
    private Button butSubmit;
    private ImageView butInfo;
    private EditText frmTitle, frmDes;
    private FirebaseAuth fAuth;
    private FirebaseFirestore devlpDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_develop);
        butSubmit = findViewById(R.id.dvlpSubmitBut);
        butSubmit.setOnClickListener(this);
        butInfo = findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        frmTitle = findViewById(id.frmDvlpTitle);
        frmDes = findViewById(id.frmDvlpDes);
        frmTitle.setOnClickListener(this);
        frmDes.setOnClickListener(this);
        fAuth = FirebaseAuth.getInstance();
        devlpDB = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {

            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            String txtTitle = frmTitle.getText().toString();
            String txtDes = frmDes.getText().toString();
            String uID = fAuth.getCurrentUser().getUid();
            String userMail = fAuth.getCurrentUser().getEmail();


            if (TextUtils.isEmpty(txtTitle)) {
                frmTitle.setError("Skill Title Needed");

            }
            if (TextUtils.isEmpty(txtDes)) {
                frmDes.setError("Skill Description Needed");
            }

            DocumentReference devlpRef = devlpDB.collection("Developers").document(uID + " | " + txtTitle);
            Map<String, Object> devlp = new HashMap<>();
            devlp.put("Dvlp Title: ", txtTitle);
            devlp.put("Dvlp Mail: ", userMail);
            devlp.put("Dvlp ID: ", uID);
            devlp.put("Dvlp Description:", txtDes);
            devlpRef.set(devlp);

            Toast.makeText(getApplicationContext(), "Thank You For Helping!", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(this, HomeScreen.class);
            startActivity(intentLogin);
            finish();
        }

    }


}

