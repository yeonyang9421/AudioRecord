package com.example.edu.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_AUDIO = 1;

    public static final int REQUEST_CODE = 2;
    public static final int REQUEST_CODE_WRITE = 100;
    Button mplay, mstop, mrecord;
    MediaRecorder mediaRecorder;
    String outputFile = null;
    MediaPlayer m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requirespermission();

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3pg";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mediaRecorder.setOutputFile(outputFile);


        mplay = findViewById(R.id.buttonPlay);
        mplay.setOnClickListener(this);
        mstop = findViewById(R.id.buttonStop);
        mstop.setOnClickListener(this);
        mrecord = findViewById(R.id.buttonRecord);
        mrecord.setOnClickListener(this);
        mplay.setEnabled(false);
        mstop.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRecord:
                Toast.makeText(getApplicationContext(), "녹음을 시작합니다.", Toast.LENGTH_LONG).show();
                try {
                    mediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
                mrecord.setEnabled(false);
                mstop.setEnabled(true);
                break;

            case R.id.buttonStop:
                mstop.setEnabled(false);
                mplay.setEnabled(true);
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                break;

            case R.id.buttonPlay:
                m = new MediaPlayer();
                try {

                    m.setDataSource(outputFile);
                    m.prepare();
                    m.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    final int PERMISSION_REQUEST_CODE = 10;
    void requirespermission() {
        String[] permissions = new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> requestPermissions = new ArrayList<>();

        for(String permission: permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                requestPermissions.add(permission);
        }
        if(!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean granted = true;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for(int i=0; i<grantResults.length; i++) {
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED) {
                        granted = false;
                        System.out.println("****************** granted: false");
                    }
                }
        }
    }
}


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            Log.i("", "Permission has been granted by user");
//        }
//
//    }



