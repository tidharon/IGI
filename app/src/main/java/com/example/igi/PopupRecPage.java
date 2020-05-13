package com.example.igi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PopupRecPage extends AppCompatActivity {
    private DisplayMetrics dm;
    private int width, height;
    private Button butStartRec, butPlayRec, butPauseRec;
    private MediaRecorder myAudioRecorder;
    private String outputFile, lastAct, lastID, recURL, lastTitle;
    private FirebaseStorage storage;
    private StorageReference recRef;
    private StorageMetadata metadata;
    private UploadTask uploadTask;
    private ProgressBar PBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_rec_page);

        setDisplay();
        /*
        recording process
         */

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
        storage = FirebaseStorage.getInstance();
        recRef = storage.getReference(lastAct + " Records").child(lastTitle);
        outputFile = (lastAct + " | " + lastID + " | " + dateFormat.toString() + ".3gp");
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);


        recProcess();
    }

    /*
    set background size
     */
    protected void setDisplay() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.4));
    }

    protected void recProcess() {
        butStartRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
                butStartRec.setEnabled(false);
                butPauseRec.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        butPauseRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PBR.setVisibility(View.VISIBLE);
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;

                    metadata = new StorageMetadata.Builder().setCustomMetadata(lastID, outputFile).build();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] daata = baos.toByteArray();
                    uploadTask = recRef.putBytes(daata, metadata);
                    recURL = recRef.getDownloadUrl().toString();
                    getIntent().putExtra("recURL", recURL);
                    PBR.setVisibility(View.INVISIBLE);
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
                    mediaPlayer.setDataSource(recURL);
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
