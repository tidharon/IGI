package com.example.igi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity implements OnClickListener {
    private Button butSignUp;
    private ImageView butInfo;
    private EditText editEmail;
    private EditText editName;
    private EditText editPassword;
    private EditText editPhone;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fsInfo;
    private ProgressBar PBSignup;
    private String email;
    private String name;
    private String uID;
    private String phone;
    private DocumentReference documentReference;
    private Map<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        butSignUp = (Button) findViewById(R.id.ButSingUp);
        butSignUp.setOnClickListener(this);
        butInfo = (ImageView) findViewById(R.id.info_but);
        butInfo.setOnClickListener(this);
        editEmail = findViewById(R.id.fldEmail);
        editName = findViewById(R.id.fldUsename);
        editPassword = findViewById(R.id.fldPassword);
        editPhone = findViewById(R.id.fldPhone);
        PBSignup = findViewById(R.id.Signup_progressBar);

        fAuth = FirebaseAuth.getInstance();
        fsInfo = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        }
    }

    public void onClick(View v) {
        if (v == butSignUp) {
            PBSignup.setVisibility(View.VISIBLE);

            email = editEmail.getText().toString().trim();
            name = editName.getText().toString();
            String password = editPassword.getText().toString().trim();
            phone = editPhone.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                editEmail.setError("Please Enter Email");
                return;
            }
            if (TextUtils.isEmpty(name)) {
                editName.setError("Please enter Your Username");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editPassword.setError("please enter password");
                return;
            }
            if (TextUtils.isEmpty(phone)){
                editPhone.setError("Please Enter Your Phone Number");
                return;
            }

            //here we create a new user based by the values he submitted above:
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(signup.this, "Welcome!", Toast.LENGTH_SHORT).show();
                        uID = fAuth.getCurrentUser().getUid();
                        //here we create a path to save the information about the new user:
                        documentReference = fsInfo.collection("UsersInfo").document(uID);
                        //here we save all the info into a single object who designed for it:
                        user = new HashMap<>();
                        user.put("UserName", name);
                        user.put("Email", email);
                        user.put("Phone Number", phone);
                        //here we put the information file into the path location and send a message to the developer approving it worked well:
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "User Profile Created For " + uID);
                            }
                        });

                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        finish();
                    } else {
                        Toast.makeText(signup.this, "ERROR! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        PBSignup.setVisibility(View.INVISIBLE);
                    }
                }
            });


        }
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
    }
}
