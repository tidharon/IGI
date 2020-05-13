package com.example.igi;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private String outputFile, lastAct, lastID, recURL;
    private FirebaseStorage storage;
    private StorageReference recRef;
    private StorageMetadata metadata;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_rec_page);

        setDisplay();
        /*
        recording process
         */
        lastAct = getIntent().getAction();
        lastID = getIntent().getStringExtra("from");
        butStartRec = (Button) findViewById(R.id.startRecBut);
        butPauseRec = (Button) findViewById(R.id.pauseRecBut);
        butPlayRec = (Button) findViewById(R.id.playRecBut);
        butPauseRec.setEnabled(false);
        butPlayRec.setEnabled(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        storage = FirebaseStorage.getInstance();
        recRef = storage.getReference(lastAct+" Records").child(lastID);
        outputFile = ("gs://igi-app-39812.appspot.com/"+lastAct+" Records/"+lastID)+("/record for " + lastAct +" | "+ lastID +" | "+ dateFormat + ".3gp");
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        recURL = recRef.getDownloadUrl().toString();
        getIntent().putExtra("recURL", recURL);
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
                    myAudioRecorder.stop();
                    myAudioRecorder.release();

                    metadata = new StorageMetadata.Builder().setCustomMetadata(lastID, outputFile).build();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] daata = baos.toByteArray();
                    uploadTask = recRef.putBytes(daata,metadata);

                    myAudioRecorder = null;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                butStartRec.setEnabled(true);
                butPauseRec.setEnabled(false);
                butPlayRec.setEnabled(true);


                Toast.makeText(getApplicationContext(), "Audio Recorded successfully", Toast.LENGTH_SHORT).show();
            }
        });

        butPlayRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
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
