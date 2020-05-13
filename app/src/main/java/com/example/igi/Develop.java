package com.example.igi;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.igi.R.*;

public class Develop extends AppCompatActivity implements View.OnClickListener {
    private Button butSubmit;
    private ImageView butInfo;
    private EditText frmTitle, frmDes;
    private FileOutputStream devFile;
    private String txtTitle, txtDes, userMail;
    private int num = 0;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private String uID;
    private DatabaseReference devlpRef;
    private String TAG = "igi";
    private FirebaseDatabase devlpDB;
    private Map<String, Object> devlp;



    //git add
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
        fAuth = FirebaseAuth.getInstance();
        devlpDB = FirebaseDatabase.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {

            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            txtTitle = frmTitle.getText().toString();
            txtDes = frmDes.getText().toString();
            uID = fAuth.getCurrentUser().getUid();
            userMail = fAuth.getCurrentUser().getEmail();

            if (TextUtils.isEmpty(txtTitle)) {
                frmTitle.setError("Skill Title Needed");

            }
            if (TextUtils.isEmpty(txtDes)) {
                frmDes.setError("Skill Description Needed");
            }

            devlpRef = devlpDB.getReference("Developers Text").child(uID +" | "+ txtTitle);
            devlp = new HashMap<>();
            devlp.put("Dvlp Title: ", txtTitle);
            devlp.put("Dvlp Mail: ", userMail);
            devlp.put("Dvlp Description:", txtDes);
            devlpRef.setValue(devlp);

            Toast.makeText(getApplicationContext(), "Thank You For Helping!", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(this, HomeScreen.class);
            startActivity(intentLogin);
            finish();
        }

    }


}

