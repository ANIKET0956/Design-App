package com.example.androidhive;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;


import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.androidhive.app.AppConfig;

import java.net.URLEncoder;

public class StreamingMp3Player extends Activity implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;


    private ImageButton buttonPlayPause;
    private SeekBar seekBarProgress;
    public TextView editTextSongURL;

    private final Handler handler = new Handler();
    private String url_object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_page);

        ImageLoader imageLoader;
        Intent intent = getIntent();
        String message = intent.getStringExtra("Title");
        TextView Text = (TextView) findViewById(R.id.songTitle);
        Text.setText(message);
        ImageView thumb_image=(ImageView)findViewById(R.id.imageView2);
        thumb_image.setImageResource(R.drawable.par1);
        String msg_url = intent.getStringExtra("Url Object");
        url_object = AppConfig.DOWNLOAD_SOURCE_URL.concat(msg_url);
        Log.d("show the url",url_object);
        initView();

    }

    private void initView() {
        buttonPlayPause = (ImageButton) findViewById(R.id.btnPlay);
        buttonPlayPause.setOnClickListener(this);

        seekBarProgress = (SeekBar) findViewById(R.id.songProgressBar);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }


    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 500);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlay) {
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
                url_object= url_object.replaceAll(" ", "%20");
                Log.d("url set",url_object);
                mediaPlayer.setDataSource(url_object); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                buttonPlayPause.setImageResource(R.drawable.btn_play);
            } else {
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(R.drawable.btn_pause);
            }

            primarySeekBarProgressUpdater();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.songProgressBar) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        buttonPlayPause.setImageResource(R.drawable.btn_play);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        // seekBarProgress.setSecondaryProgress(percent);
    }
}
