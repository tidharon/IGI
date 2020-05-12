package com.example.igi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

public class Solution extends AppCompatActivity implements View.OnClickListener {
    private Button butPhotoSltn;
    private Button butRecSltn;
    private ImageView butInfo;
    private Button butSubmit;
    private Spinner listProblem;
    private EditText editTitle, editDesc;
    private ArrayAdapter<String> problemArray;
    private String TAG = "igi";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String txtTitle, txtDes, uID, imgPath;
    private FirebaseStorage storage;
    private StorageReference imgRef;
    private StorageMetadata metadata;
    private UploadTask upTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        butPhotoSltn = (Button) findViewById(R.id.butPhotoSltn);
        butPhotoSltn.setOnClickListener(this);
        butRecSltn = (Button) findViewById(R.id.butRecSltn);
        butRecSltn.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        butSubmit = (Button) findViewById(R.id.submit_but);
        butSubmit.setOnClickListener(this);
        {
            listProblem = (Spinner) findViewById(R.id.problemList);
            problemArray = new ArrayAdapter<>(Solution.this, android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.problemList));
            problemArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listProblem.setAdapter(problemArray);
        }
        editTitle = findViewById(R.id.frmPrblmTitle);
        editDesc = findViewById(R.id.frmPrblmDes);

    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butPhotoSltn) {
            TakePicture();
        }
        if (v == butRecSltn) {

            Intent intentDiscover = new Intent(this, PopupRecPage.class).putExtra("from", "solution");
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            Toast.makeText(getApplicationContext(), "Your Great Idea Added To Data Base!", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(this, HomeScreen.class);
            startActivity(intentLogin);
            finish();
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void TakePicture() {
        txtTitle = editTitle.getText().toString();
        if (TextUtils.isEmpty(txtTitle)) {
            editTitle.setError("Solution Title Needed");
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        Toast.makeText(getApplicationContext(), "Image Saved Successfully!", Toast.LENGTH_SHORT).show();
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
            String photoFile = "sltnImage" + "_" + txtTitle + "_" + date + ".jpg";
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            byte[] daata = boas.toByteArray();
            //addImageFile(mainPicture);
            imgRef = storage.getReference("Solutions Pictures").child(txtTitle + "_" + uID);
            metadata = new StorageMetadata.Builder().setCustomMetadata("solution: ", photoFile).build();
            upTask = imgRef.putBytes(daata, metadata);
            Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();

        }
    }
}