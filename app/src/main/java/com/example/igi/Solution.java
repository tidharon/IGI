package com.example.igi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Solution extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_ID = 666; // the number doesn't matter
    private Button butPhotoSltn;
    private Button butRecSltn;
    private ImageView butInfo;
    private Button butSubmit;
    private Spinner listProblem;
    private EditText editTitle, editDesc;
    private List<String> problemArray;
    private String TAG = "igi";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String txtTitle;
    private String uID;
    private String prblmName;
    private String sltnID;
    private String imageURL;
    private String recURL;
    private FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private ProgressBar SPB;
    private FirebaseDatabase prblmDB;
    private DatabaseReference sltnRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        imageURL = "Not Taken";
        recURL = "Not Taken";

        butPhotoSltn = (Button) findViewById(R.id.butPhotoSltn);
        butPhotoSltn.setOnClickListener(this);
        butRecSltn = (Button) findViewById(R.id.butRecSltn);
        butRecSltn.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        butSubmit = (Button) findViewById(R.id.submit_but);
        butSubmit.setOnClickListener(this);
        SPB = findViewById(R.id.PBSolution);
        prblmDB = FirebaseDatabase.getInstance();

        {
            //TODO check why int java.util.List.size() is null
            /*
            listProblem = (Spinner) findViewById(R.id.problemList);
            int num = 5;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, num, problemTitles());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listProblem.setAdapter(adapter);

             */
        }
        fAuth = FirebaseAuth.getInstance();
        editTitle = findViewById(R.id.frmSltnTitle);
        editDesc = findViewById(R.id.frmSltnTitle);
        uID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        storage = FirebaseStorage.getInstance();
        sltnID = prblmDB.getReference().push().getKey();

    }

    @Override
    public void onClick(View v) {
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
        if (v == butPhotoSltn) {
            SPB.setVisibility(View.VISIBLE);
            txtTitle = editTitle.getText().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Solution Title Needed");
                SPB.setVisibility(View.INVISIBLE);
                return;
            }

            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
            SPB.setVisibility(View.INVISIBLE);
        }
        if (v == butRecSltn) {
            txtTitle = editTitle.getText().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
                SPB.setVisibility(View.INVISIBLE);
                return;
            }
            sltnRef = prblmDB.getReference("Problems").child(prblmName).child("Solution: "+txtTitle);
            sltnID = sltnRef.getKey();
            Intent intentDiscover = new Intent(this, PopupRecPage.class);
            intentDiscover.putExtra("from", sltnID);
            intentDiscover.putExtra("title", txtTitle);
            intentDiscover.putExtra("sl/pr", "Solution");
            startActivity(intentDiscover);
            recURL = getIntent().getStringExtra("recURL");
        }
        if (v == butSubmit) {
            submission();
        }
    }

    public List<String> problemTitles() {
        DatabaseReference prblmRef = prblmDB.getReference("Problems");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    problemArray.add(ds.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        prblmRef.addValueEventListener(eventListener);
        return problemArray;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_ID) {
            txtTitle = editTitle.getText().toString();
            prblmName = "nothing works";
            sltnRef = prblmDB.getReference("Problems").child(prblmName).child("Solution: " + txtTitle);
            Bitmap imageBitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = "Solution | " + sltnID + " | " + date + ".jpg";
            byte[] daata = baos.toByteArray();

            final StorageReference imgRef = storage.getReference("Solutions Pictures").child(txtTitle);
            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("sltn: ", photoFile).build();
            UploadTask upTask = imgRef.putBytes(daata, metadata);
            imageURL = imgRef.toString();

            Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void submission() {
        SPB.setVisibility(View.VISIBLE);
        txtTitle = editTitle.getText().toString();
        if (TextUtils.isEmpty(txtTitle)) {
            editTitle.setError("Solution Title Needed");
            SPB.setVisibility(View.INVISIBLE);
            return;
        }
        String txtDes = editDesc.getText().toString();
        if (TextUtils.isEmpty(txtDes)) {
            editDesc.setError("Solution Description Needed");
            SPB.setVisibility(View.INVISIBLE);
            return;
        }
        uID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        //prblmName = listProblem.getSelectedItem().toString();
        /*if (TextUtils.isEmpty(prblmName)) {
            Toast.makeText(getApplicationContext(), "You Must Choose Problem First!", Toast.LENGTH_SHORT).show();
            SPB.setVisibility(View.INVISIBLE);
            return;
        }*/
        prblmName = "nothing works";
        sltnRef = prblmDB.getReference("Problems").child(prblmName).child("Solution: "+txtTitle);
        Map<String, Object> sltn = new HashMap<>();
        sltn.put("Sltn ID: ", sltnID);
        sltn.put("Server ID: ", uID);
        sltn.put("Sltn Title: ", txtTitle);
        sltn.put("Sltn Description: ", txtDes);
        sltn.put("Sltn Image: ", imageURL);
        sltn.put("Sltn Record: ", recURL);
        sltnRef.setValue(sltn);

        SPB.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "Your Great Idea Added To Data Base!", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(this, HomeScreen.class);
        startActivity(intentLogin);
        finish();
    }

}

