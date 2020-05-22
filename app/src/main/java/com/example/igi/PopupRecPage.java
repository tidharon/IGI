package com.example.igi;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
    private StorageMetadata metadata;
    private UploadTask uploadTask;
    private ProgressBar PBR;
    private FirebaseStorage storage;
    private String lastAct;
    private String lastTitle;

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
        String date = dateFormat.format(new Date());
        storage = FirebaseStorage.getInstance();
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + ("/" + lastAct + " | " + lastID + " | " + date + ".3gp");
        uploadFile = lastAct + " | " + lastID + " | " + date + ".3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        //TODO from the emulator the records go as .3gp file but 0KB weight and from external device still upload only "application/octet-stream" file.
        recProcess();
    }

    /*
    set background size
     */
    protected void setDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

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

                    recRef = storage.getReference(lastAct + " Records").child(lastTitle+"/"+uploadFile);
                    metadata = new StorageMetadata.Builder().setCustomMetadata(lastID, outputFile).build();


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] daata = baos.toByteArray();
                    uploadTask = (UploadTask) recRef.putBytes(daata, metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            recURL = recRef.toString();
                            getIntent().putExtra("recURL", recURL);
                            myAudioRecorder = null;
                            PBR.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("upload failed because", Objects.requireNonNull(e.getMessage()));
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
