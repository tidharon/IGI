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
    private String titleText, desText, allText;
    private int num = 0;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private String IdDetails;
    private DocumentReference documentReference;
/////

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
            String TxtTitle = frmTitle.getText().toString();
            String TxtDes = frmDes.getText().toString();

            if (TextUtils.isEmpty(TxtTitle)) {
                frmTitle.setError("Please Enter Title");
            }
            if (TextUtils.isEmpty(TxtDes)) {
                frmDes.setError("Please enter Your Description");
            }

            IdDetails = fAuth.getCurrentUser().getUid();
            documentReference = fstore.collection("Developers").document(TxtTitle + " " + TxtDes);
            final Map<String, String> develop = new HashMap<>();
            develop.put("Develop Title " , TxtTitle);
            develop.put("Develop Description " , TxtDes);
            documentReference.set(develop).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG , "Developer info saved." + develop);
                }
            })

            Toast.makeText(getApplicationContext(), "Thank You For Helping!", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(this, HomeScreen.class);
            startActivity(intentLogin);
            finish();
        }

    }


    }
}
