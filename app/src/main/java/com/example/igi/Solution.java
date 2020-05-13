package com.example.igi;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class Solution extends AppCompatActivity implements View.OnClickListener {
    private Button butPhotoSltn;
    private Button butRecSltn;
    private ImageView butInfo;
    private Button butSubmit;
    private Spinner listProblem;
    private EditText editTitle, editDesc;
    private List<String> problemArray;
    private ArrayAdapter<CharSequence> adapter;
    private String TAG = "igi";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String txtTitle, txtDes, uID, prblmName, sltnID, imageURL, recURL;
    private FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private StorageReference imgRef;
    private StorageMetadata metadata;
    private UploadTask upTask;
    private ProgressBar SPB;
    private ByteArrayOutputStream baos;
    private FirebaseDatabase prblmDB;
    private DatabaseReference prblmRef, sltnRef;
    private Map<String, Object> sltn;
    private ValueEventListener eventListener;
    private static final int PICK_IMAGE_ID = 666; // the number doesn't matter


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
        {
            listProblem = (Spinner) findViewById(R.id.problemList);
            adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, problemTitles());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listProblem.setAdapter(adapter);
        }
        fAuth = FirebaseAuth.getInstance();
        editTitle = findViewById(R.id.frmPrblmTitle);
        editDesc = findViewById(R.id.frmPrblmDes);
        prblmDB = FirebaseDatabase.getInstance();
        sltnID = sltnRef.getKey();
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
            sltnRef = prblmDB.getReference("Problems Text").child(uID +" | "+ txtTitle);
            sltnID = sltnRef.getKey();
            Intent intentDiscover = new Intent(this, PopupRecPage.class).putExtra("from", sltnID);
            startActivity(intentDiscover);
            recURL = getIntent().getStringExtra("recURL");
        }
        if (v == butSubmit) {
            submission();
        }
    }

    public List<String> problemTitles() {
        prblmRef = prblmDB.getReference("Solutions");
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    problemArray.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        prblmRef.addListenerForSingleValueEvent(eventListener);
        return problemArray;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap imageBitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = dateFormat.format(new Date());
                String photoFile = "sltnImage" + "_" + txtTitle + "_" + date + ".jpg";
                byte[] daata = baos.toByteArray();

                imgRef = storage.getReference("Solutions pictures").child(txtTitle + " | " + sltnID);
                metadata = new StorageMetadata.Builder().setCustomMetadata("sltn: ", photoFile).build();
                upTask = imgRef.putBytes(daata, metadata);
                imageURL = imgRef.getDownloadUrl().toString();

                Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
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
        txtDes = editDesc.getText().toString();
        if (TextUtils.isEmpty(txtDes)) {
            editDesc.setError("Solution Description Needed");
            SPB.setVisibility(View.INVISIBLE);
            return;
        }
        uID = fAuth.getCurrentUser().getUid();
        prblmName = listProblem.getSelectedItem().toString();
        if (TextUtils.isEmpty(prblmName)) {
            Toast.makeText(getApplicationContext(), "You Must Choose Problem First!", Toast.LENGTH_SHORT).show();
            SPB.setVisibility(View.INVISIBLE);
            return;
        }
        sltnRef = prblmDB.getReference("Solutions").child(prblmName).child(txtTitle);
        sltn = new HashMap<>();
        sltn.put("Solution ID: ", sltnID);
        sltn.put("Solution Title: ", txtTitle);
        sltn.put("Solution Description: ", txtDes);
        sltn.put("Solution Image URL: ", imageURL);
        sltn.put("Solution Record URL: ", recURL);
        sltnRef.setValue(sltn);

        SPB.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "Your Great Idea Added To Data Base!", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(this, HomeScreen.class);
        startActivity(intentLogin);
        finish();
    }

}

