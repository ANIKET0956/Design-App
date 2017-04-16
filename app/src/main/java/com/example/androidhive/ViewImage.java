package com.example.androidhive;

/**
 * Created by SAHIL on 4/1/2017.
 */


import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidhive.app.AppConfig.DOWNLOAD_SOURCE_URL;
import static com.example.androidhive.app.AppConfig.SERVER_URL;


public class ViewImage extends Activity {
    private long enqueue;
    private DownloadManager dm;
    private String downloadUrl = SERVER_URL;
    private String sourceUrl = SERVER_URL;
    private static ImageView iv;
    private static Bitmap bmp;
    private static Button bDownload;
    private static BroadcastReceiver receiver;
    private ImageLoader image_loader;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_image);
        String uri = getIntent().getStringExtra("uri");
        System.out.println("ViewImage activity: " + uri);
        downloadUrl = downloadUrl + uri;
        iv = (ImageView) findViewById(R.id.imageView1);
        image_loader = new ImageLoader(getApplicationContext(),this);
//        image_loader.DisplayImage(downloadUrl,iv);
        LoadImage(downloadUrl);

        bDownload = (Button) findViewById(R.id.button2);
        bDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                download(downloadUrl);
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        iv = null;
        bmp = null;
        bDownload = null;
        receiver = null;
        try {
            unregisterReceiver(receiver);
        }
        catch (Exception e){
            Log.e("onDestroy viewImage: ", e.toString());
        }
    }

    public static void LoadImage(final String url) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(url).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                    Log.e("Load Image error:", e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null) {
                    iv.setImageBitmap(bmp);
                }
                else{
                    System.out.println("null bitmap!!");
                }
            }

        }.execute();
    }

    public void download(final String downloadUrl){
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse(downloadUrl));
        enqueue = dm.enqueue(request);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        unregisterReceiver(receiver);
    }
}