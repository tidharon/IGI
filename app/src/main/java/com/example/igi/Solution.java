package com.example.igi;

import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

public class Solution extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_ID = 666; // the number doesn't matter
    private Button butPhotoSltn;
    private Button butRecSltn;
    private ImageView butInfo;
    private Button butSubmit;
    private EditText editTitle, editDesc;
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
    private FirebaseFirestore prblmDB;
    private DocumentReference sltnRef;

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
        prblmDB = FirebaseFirestore.getInstance();

        {
            //TODO check why nothing appears

            Spinner listProblem = findViewById(R.id.problemList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, problemTitles());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listProblem.setAdapter(adapter);

            listProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    prblmName = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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
            sltnRef = prblmDB.collection("Problems").document(prblmName).collection("Solutions").document(txtTitle);
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


    public List<String> problemTitles() {
        final List<String> problemArray = new List<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return null;
            }

            @Override
            public String set(int index, String element) {
                return null;
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        CollectionReference prblmRef = prblmDB.collection("Problems");
        /*prblmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;

                Log.e("Count ", "" + dataSnapshot.getChildrenCount());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {        //TODO the data now arrive and show on debug but not on list.get
                    Log.println(Log.DEBUG, "round number", String.valueOf((i++)));
                    if (ds.getValue() == null) {

                        break;
                    }

                    Log.d("is the list empty: ", String.valueOf(problemArray.get(0)));
                    problemArray.add(ds.getKey());
                    Log.println(Log.INFO, "round value", ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("the read failed: ", databaseError.getMessage());
            }
        });*/
        Log.println(Log.DEBUG, "ValueEventListener: ", "Done");
        SPB.setVisibility(View.INVISIBLE);
        Log.i("list value", problemArray.toString());
        return problemArray;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_ID) {
            txtTitle = editTitle.getText().toString();
            sltnRef = prblmDB.collection("Problems").document(prblmName).collection("Solutions").document(txtTitle);
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
        //prblmName = "nothing works";
        sltnRef = prblmDB.collection("Problems").document(prblmName).collection("Solutions").document(txtTitle);
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

