package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Problem extends AppCompatActivity implements View.OnClickListener {
    private Button butPhotoPrblm;
    private Button butRecPrblm;
    private ImageView butInfo;
    private Button butSubmit;
    private EditText editTitle, editDesc;
    private String TAG = "igi";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String txtTitle, txtDes, uID;
    private FirebaseDatabase prblmDB;
    private DatabaseReference prblmRef, prblmTTL;
    private Map<String, Object> prblm;
    private FirebaseAuth fAuth;
    private ProgressBar PBP;
    private FirebaseStorage storage;
    private StorageReference imgRef;
    private StorageMetadata metadata;
    private UploadTask upTask;
    private ArrayList<String> prblmTitle;
    private ByteArrayOutputStream baos;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        butPhotoPrblm = (Button) findViewById(R.id.butPhotoPrblm);
        butPhotoPrblm.setOnClickListener(this);
        butRecPrblm = (Button) findViewById(R.id.butRecPrblm);
        butRecPrblm.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        butSubmit = (Button) findViewById(R.id.submit_but);
        butSubmit.setOnClickListener(this);
        editTitle = findViewById(R.id.frmPrblmTitle);
        editDesc = findViewById(R.id.frmPrblmDes);
        prblmDB = FirebaseDatabase.getInstance();
        PBP = findViewById(R.id.PBPrblm);
        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butPhotoPrblm) {
            PBP.setVisibility(View.VISIBLE);
            txtTitle = editTitle.getText().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
                PBP.setVisibility(View.INVISIBLE);
            }
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
            PBP.setVisibility(View.INVISIBLE);
        }
        if (v == butRecPrblm) {
            txtTitle = editTitle.getText().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
            }
            Intent intentDiscover = new Intent(this, PopupRecPage.class).putExtra("from", txtTitle);
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            PBP.setVisibility(View.VISIBLE);
            submission();
            PBP.setVisibility(View.INVISIBLE);
        }
    }

    private void submission() {
        txtTitle = editTitle.getText().toString();
        txtDes = editDesc.getText().toString();
        uID = fAuth.getCurrentUser().getUid();


        if (TextUtils.isEmpty(txtTitle)) {
            editTitle.setError("Problem Title Needed");

        }
        if (TextUtils.isEmpty(txtDes)) {
            editDesc.setError("Problem Description Needed");
        }

        prblmRef = prblmDB.getReference("Problems Text").child(uID +" | "+ txtTitle);
        prblm = new HashMap<>();
        prblm.put("Prblm Title: ", txtTitle);
        prblm.put("Prblm Description:", txtDes);
        prblmRef.setValue(prblm);
        //prblmRef = prblmDB.getReference("Problems Text").child("Titles");

        //prblmTitle.add(txtTitle);

        Toast.makeText(getApplicationContext(), "Problem Saved!", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(this, HomeScreen.class);
        startActivity(intentLogin);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap imageBitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = dateFormat.format(new Date());
                String photoFile = "prblmImage" + "_" + txtTitle + "_" + date + ".jpg";
                byte[] daata = baos.toByteArray();

                imgRef = storage.getReference("Problems Pictures").child(txtTitle + "_" + uID);
                metadata = new StorageMetadata.Builder().setCustomMetadata("prblm: ", photoFile).build();
                upTask = imgRef.putBytes(daata, metadata);


                Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
