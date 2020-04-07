package com.example.igi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

public class Solution extends AppCompatActivity implements View.OnClickListener {
    Button butPhotoSltn;
    Button butRecSltn;
    ImageView butInfo;
    Button butSubmit;
    Spinner listProblem;
    ArrayAdapter<String> problemArray;
    String TAG = "igi";


    @SuppressLint("WrongViewCast")
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
        listProblem = (Spinner) findViewById(R.id.problemList);
        problemArray = new ArrayAdapter<>(Solution.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.problemList));
        problemArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listProblem.setAdapter(problemArray);
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
        if (v==butRecSltn){
            Intent intentDiscover = new Intent(this, PopupRecPage.class);
            startActivity(intentDiscover);
        }
        if (v == butSubmit) {
            Toast.makeText(getApplicationContext(), "Your Great Idea Added To Data Base!", Toast.LENGTH_SHORT).show();
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void TakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //img.setImageBitmap(imageBitmap);

            File pictureFileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IGI/");
            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                return;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = "prblmImage" + "_" + date + ".jpg";
            String filename = pictureFileDir.getPath() + File.separator + photoFile;
            File mainPicture = new File(filename);
            //addImageFile(mainPicture);

            try {
                FileOutputStream fos = new FileOutputStream(mainPicture);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Log.d(TAG, "image saved in" + filename);
            } catch (Exception error) {
                Log.d(TAG, "Image could not be saved");
            }
            Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();

        }
    }
}