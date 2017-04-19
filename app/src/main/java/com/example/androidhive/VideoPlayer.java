package com.example.androidhive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.example.androidhive.app.AppConfig;

public class VideoPlayer extends AppCompatActivity {


    private MediaPlayer mediaPlayer;
    private VideoView mVideoView;
    private MediaController mMediaController;
    private int position = 0;
    SeekBar mSeekBar;
    String path="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        mMediaController = new MyMediaController(this);

        mVideoView = (VideoView) findViewById(R.id.videoview);

        Intent intent = getIntent();
        String path = intent.getStringExtra("url object");
        path = AppConfig.DOWNLOAD_SOURCE_URL.concat(path).replaceAll(" ", "%20");
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test);
        uri = Uri.parse(path);
        mVideoView.setVideoURI(uri);
        mVideoView.setMediaController(mMediaController);


        final int video_duration;
        mMediaController.setPrevNextListeners(new View.OnClickListener() {
            public void onClick(View v) {
                // v is mc
                // code for next
            }
        }, new View.OnClickListener() {
            public void onClick(View v) {
                // v is mc
                // code for previous
            }
        });

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (mVideoView.isPlaying()) {

                    mVideoView.pause();
                    return false;
                } else {

                    mVideoView.start();
                    return false;
                }
            }

        });


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                final int topContainerId1 = getResources().getIdentifier("mediacontroller_progress", "id", "android");
                mediaPlayer.start();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }




}
