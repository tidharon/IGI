package com.example.igi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Problem extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    public String prblmID;
    private Button butPhotoPrblm;
    private Button butRecPrblm;
    private ImageView butInfo;
    private Button butSubmit;
    private EditText editTitle;
    private EditText editDesc;
    private String TAG = "igi";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String txtTitle;
    private String uID;
    private String imageURL;
    private String recURL;
    private FirebaseFirestore prblmDB;
    private DocumentReference prblmRef;
    private ProgressBar PBP;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        imageURL = "Not Taken";
        recURL = "Not Taken";
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
        prblmDB = FirebaseFirestore.getInstance();
        PBP = findViewById(R.id.PBPrblm);
        storage = FirebaseStorage.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        uID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        //prblmID = prblmDB.collection("Problems").document().getId();


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
                return;
            }
            prblmRef = prblmDB.collection("Problems").document(txtTitle);
            prblmID = prblmRef.getId();
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

        }
        if (v == butRecPrblm) {
            txtTitle = editTitle.getText().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
                PBP.setVisibility(View.INVISIBLE);
                return;
            }
            prblmRef = prblmDB.collection("Problems").document(txtTitle);
            prblmID = prblmRef.getId();
            Intent intentDiscover = new Intent(this, PopupRecPage.class);
            intentDiscover.putExtra("from", prblmID);
            intentDiscover.putExtra("title", txtTitle);
            intentDiscover.putExtra("sl/pr", "Problem");
            startActivity(intentDiscover);
            recURL = getIntent().getStringExtra("recURL");
        }
        if (v == butSubmit) {
            PBP.setVisibility(View.VISIBLE);
            submission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                txtTitle = editTitle.getText().toString();
                prblmRef = prblmDB.collection("Problems").document(txtTitle);
                prblmID = prblmRef.getId();
                Bitmap imageBitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = dateFormat.format(new Date());
                String photoFile = "Problem" + " | " + prblmID + " | " + date + ".jpg";
                byte[] daata = baos.toByteArray();

                prblmRef = prblmDB.collection("Problems").document(txtTitle);
                StorageReference imgRef = storage.getReference("Problems Pictures").child(txtTitle);
                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("prblm: ", photoFile).build();
                UploadTask upTask = imgRef.putBytes(daata, metadata);
                imageURL = imgRef.toString();


                Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                PBP.setVisibility(View.INVISIBLE);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                PBP.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void submission() {
        PBP.setVisibility(View.VISIBLE);

        txtTitle = editTitle.getText().toString();
        String txtDes = editDesc.getText().toString();

        if (TextUtils.isEmpty(txtTitle)) {
            editTitle.setError("Problem Title Needed");
            return;
        }
        if (TextUtils.isEmpty(txtDes)) {
            editDesc.setError("Problem Description Needed");
            return;
        }

        prblmRef = prblmDB.collection("Problems").document(txtTitle);
        prblmID = prblmRef.getId();
        Map<String, Object> prblm = new HashMap<>();
        prblm.put("Prblm ID: ", prblmID);
        prblm.put("Server ID: ", uID);
        prblm.put("Prblm Title: ", txtTitle);
        prblm.put("Prblm Description:", txtDes);
        prblm.put("Prblm Image: ", imageURL);
        prblm.put("Prblm Record: ", recURL);

        prblmRef.set(prblm);

        PBP.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "Problem Saved!", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(this, HomeScreen.class);
        startActivity(intentLogin);
        finish();
    }
}
