package com.example.igi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 1;
    private static long back_pressed;
    private Button butLogin;
    private Button butToSignUp;
    private Button butInfo;
    private EditText editUsername;
    private EditText editPassword;
    private ProgressBar PBLogin;                        //This is a variable from the layout who use for improvement of user experience
    private FirebaseAuth fAuth;                         //this is a variable used for using the

    private void requestPermission() {                  //This function request permissions from the user
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    //these objects are used to check the permissions request result
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission && CameraPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        //these objects used to make sure permissions given:
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tag = "IGI";
        Log.d(tag, "started");
            //here we make sure permissions granted:
        if (!checkPermission()) {
            requestPermission();
        }

        setContentView(R.layout.activity_main);
        butLogin = (Button) findViewById(R.id.ButLogin);
        butToSignUp = (Button) findViewById(R.id.ButToSignUp);
        butInfo = (Button) findViewById(R.id.info_but);
        butLogin.setOnClickListener(this);
        butToSignUp.setOnClickListener(this);
        butInfo.setOnClickListener(this);
        editUsername = findViewById(R.id.LoginUsername);
        editPassword = findViewById(R.id.LoginPassword);
        PBLogin = findViewById(R.id.Login_progressBar);
        fAuth = FirebaseAuth.getInstance();

            //here we transfer the user directly to the main activity in case he's already connected to a user:
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == butLogin) {

            PBLogin.setVisibility(View.VISIBLE);        //showing the user that there's a process running

            String email = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
                //makes sure details are given:
            if (TextUtils.isEmpty(email)) {
                editUsername.setError("Please enter Your Email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                editPassword.setError("please enter password");
                return;
            }
                //checks if the details are equal to certain user in the stock and singing him in
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "ERROR! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        PBLogin.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        if (v == butToSignUp) {
            Intent intentLogin = new Intent(this, signup.class);
            startActivity(intentLogin);
        }
        if (v == butInfo) {
            Intent intentDiscover = new Intent(this, InfoPage.class);
            startActivity(intentDiscover);
        }
    }

    @Override
    public void onBackPressed() {
            //checks if the user already pressed on back button ni the last 2000 milliseconds
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}

