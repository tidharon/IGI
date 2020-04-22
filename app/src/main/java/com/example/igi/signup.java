package com.example.igi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity implements OnClickListener {
    Button butSignUp;
    ImageView butInfo;
    EditText editEmail, editName, editPassword;
    FirebaseAuth fAuth;
    ProgressBar PBSignup;
    String email, name, password;

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
        PBSignup = findViewById(R.id.Signup_progressBar);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
        }
    }

    public void onClick(View v) {
        if (v == butSignUp) {
            PBSignup.setVisibility(View.VISIBLE);

            email = editEmail.getText().toString().trim();
            name = editName.getText().toString().trim();
            password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                editEmail.setError("Please Enter Email");
            }
            if (TextUtils.isEmpty(name)) {
                editName.setError("Please enter Your Username");
            }
            if (TextUtils.isEmpty(password)) {
                editPassword.setError("please enter password");
            }

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(signup.this, "Welecome!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        finish();
                    } else {
                        Toast.makeText(signup.this, "ERROR! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

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
