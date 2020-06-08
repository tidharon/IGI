package com.example.igi;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PopupRecPage extends AppCompatActivity {
    private Button butStartRec;
    private Button butPlayRec;
    private Button butPauseRec;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private String lastID;
    private String recURL;
    private String uploadFile;
    private StorageReference recRef;
    private ProgressBar PBR;
    private FirebaseStorage storage;
    private String lastAct;
    private String lastTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_rec_page);

        setDisplay();

        lastAct = getIntent().getStringExtra("sl/pr");
        lastID = getIntent().getStringExtra("from");
        lastTitle = getIntent().getStringExtra("title");
        PBR = findViewById(R.id.PBRecord);
        butStartRec = (Button) findViewById(R.id.startRecBut);
        butPauseRec = (Button) findViewById(R.id.pauseRecBut);
        butPlayRec = (Button) findViewById(R.id.playRecBut);
        butPauseRec.setEnabled(false);
        butPlayRec.setEnabled(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        storage = FirebaseStorage.getInstance();
        uploadFile = lastAct + " | " + lastID + " | " + date + ".3gp";
        //here we set the path to keep the result file locally before upload:
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + ("/" + uploadFile);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB); //this is the setup of the usage of the file for replay
        myAudioRecorder.setOutputFile(outputFile);
        recProcess();
    }

    /*
    set background size
     */
    protected void setDisplay() {
        DisplayMetrics dm = new DisplayMetrics();       //here we create an object that contains an height ang width values
        getWindowManager().getDefaultDisplay().getMetrics(dm);  //here we gives him the value of the correct user's screen
            //here we set the activity's window size to be smaller then the original so the user may press outside and close the record process:
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.4));
    }

    protected void recProcess() {
        butStartRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    Log.println(Log.ERROR, "TAG", "Not Prepare");
                }

                butStartRec.setEnabled(false);
                butPauseRec.setEnabled(true);
            }
        });

        butPauseRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PBR.setVisibility(View.VISIBLE);
                    myAudioRecorder.stop();
                    myAudioRecorder.release();

                    recRef = storage.getReference(lastAct + " Records").child(lastTitle + "/" + uploadFile);
                    Uri uri = Uri.fromFile(new File(outputFile));
                    recRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            recURL = recRef.toString();
                            //here we upload the file and getting the information we need for future use
                            getIntent().putExtra("recURL", recURL);
                            myAudioRecorder = null;
                            PBR.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("upload failed because ", Objects.requireNonNull(e.getMessage()));
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Audio Recorded successfully", Toast.LENGTH_SHORT).show();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                butStartRec.setEnabled(true);
                butPauseRec.setEnabled(false);
                butPlayRec.setEnabled(true);


            }
        });

        butPlayRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    //here take the file recorded and play it for the user's ask:
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
            }
        });
    }

}
