package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

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
    private String txtTitle, txtDes, uID, imgPath;
    private FirebaseDatabase prblmDB;
    private DatabaseReference prblmRef;
    private Map<String, Object> prblm;
    private FirebaseAuth fAuth;
    private ProgressBar PBP;
    private FirebaseStorage storage;
    private StorageReference imgRef;
    private StorageMetadata metadata;
    private UploadTask upTask;


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
    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butPhotoPrblm) {
            TakePicture();
        }
        if (v == butRecPrblm) {
            Intent intentDiscover = new Intent(this, PopupRecPage.class);
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            PBP.setVisibility(View.VISIBLE);

            txtTitle = editTitle.getText().toString();
            txtDes = editDesc.getText().toString();
            uID = fAuth.getCurrentUser().getUid();

            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
            }
            if (TextUtils.isEmpty(txtDes)) {
                editDesc.setError("Problem Description Needed");
            }

            prblmRef = prblmDB.getReference("Problems Text").child(uID + txtTitle);
            prblm = new HashMap<>();
            prblm.put("Prblm Title: ", txtTitle);
            prblm.put("Prblm Description:", txtDes);
            prblmRef.setValue(prblm);

            Toast.makeText(getApplicationContext(), "Problem Saved!", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(this, HomeScreen.class);
            startActivity(intentLogin);
            finish();
            PBP.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Take pic with ui and show in image view
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void TakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //img.setImageBitmap(imageBitmap);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            txtTitle = editTitle.getText().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
            }
            String photoFile = "prblmImage" + "_" + txtTitle + "_" + date + ".jpg";
            byte[] daata = c
            //addImageFile(mainPicture);
            imgRef = storage.getReference("Problems Pictures").child(txtTitle +"_"+ uID);
            metadata = new StorageMetadata.Builder().setCustomMetadata("prblm: ", photoFile).build();
            upTask = imgRef.putBytes(data, metadata);
            Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();

        }
    }
}
