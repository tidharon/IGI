package com.example.igi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.Objects;

public class Solution extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_ID = 666; // the number doesn't matter
    private Button butPhotoSltn;
    private Button butRecSltn;
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
    private String prblmName;
    private String sltnID;
    private String imageURL;
    private String recURL;
    private FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private ProgressBar SPB;
    //private FirebaseFirestore prblmDB;
    private DocumentReference sltnRef;
    private CollectionReference prblmDB;
    private ArrayList<String> problemList;
    private Spinner listProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Tap The Orange Top Rectangle And Choose Problem First!", Toast.LENGTH_LONG).show();
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
        prblmDB = FirebaseFirestore.getInstance().collection("Problems");

        {
            //TODO check why nothing appears on view
            //TODO check why selectedItem returns null
            listProblem = findViewById(R.id.problemList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, problemTitles());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listProblem.setAdapter(adapter);
            listProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    prblmName = listProblem.getItemAtPosition(position).toString();
                    Log.e("problem chosen", prblmName);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            try {
                prblmName = listProblem.getSelectedItem().toString();
                Log.e("problem selected", prblmName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fAuth = FirebaseAuth.getInstance();
        editTitle = findViewById(R.id.frmSltnTitle);
        editDesc = findViewById(R.id.frmSltnDes);
        uID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        storage = FirebaseStorage.getInstance();

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

            if (listProblem.getSelectedItem() == null && prblmName == null) {
                Toast.makeText(this, "You Must Choose Problem First!", Toast.LENGTH_SHORT).show();
                SPB.setVisibility(View.INVISIBLE);
                return;
            }
            prblmName = listProblem.getSelectedItem().toString();

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
            if (listProblem.getSelectedItem() == null && prblmName == null) {
                Toast.makeText(this, "You Must Choose Problem First!", Toast.LENGTH_SHORT).show();
                SPB.setVisibility(View.INVISIBLE);
                return;
            }
            prblmName = listProblem.getSelectedItem().toString();
            if (TextUtils.isEmpty(txtTitle)) {
                editTitle.setError("Problem Title Needed");
                SPB.setVisibility(View.INVISIBLE);
                return;
            }
            sltnRef = prblmDB.document(prblmName).collection("Solutions").document(txtTitle);
            sltnID = sltnRef.getId();
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


    public ArrayList<String> problemTitles() {
        problemList = new ArrayList<>();
        prblmDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        problemList.add(i++, Objects.requireNonNull(document.get("Prblm Title: ")).toString());
                        Log.d("document title: ", Objects.requireNonNull(document.get("Prblm Title: ")).toString());
                    }
                    Log.d("list length: ", String.valueOf(problemList.size()));
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
                Log.println(Log.DEBUG, "ValueEventListener: ", "Done");
                SPB.setVisibility(View.INVISIBLE);
                Log.i("list value", problemList.toString());
            }
        }).isSuccessful();
        return problemList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_ID) {
            txtTitle = editTitle.getText().toString();
            prblmName = listProblem.getSelectedItem().toString();

            sltnRef = prblmDB.document(prblmName).collection("Solutions").document(txtTitle);
            sltnID = sltnRef.getId();
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
        if (listProblem.getSelectedItem() == null && prblmName == null) {
            Toast.makeText(this, "You Must Choose Problem First!", Toast.LENGTH_SHORT).show();
            SPB.setVisibility(View.INVISIBLE);
            return;
        }
        prblmName = listProblem.getSelectedItem().toString();
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
        sltnRef = prblmDB.document(prblmName).collection("Solutions").document(txtTitle);
        Map<String, Object> sltn = new HashMap<>();
        sltn.put("Sltn ID: ", sltnID);
        sltn.put("Server ID: ", uID);
        sltn.put("Sltn Title: ", txtTitle);
        sltn.put("Sltn Description: ", txtDes);
        sltn.put("Sltn Image: ", imageURL);
        sltn.put("Sltn Record: ", recURL);
        sltnRef.set(sltn);

        SPB.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "Your Great Idea Added To Data Base!", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(this, HomeScreen.class);
        startActivity(intentLogin);
        finish();
    }


}

