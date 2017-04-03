package com.example.androidhive;

import android.app.Activity;

/**
 * Created by Aniket on 3/31/2017.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;



public class LocalSong extends Activity{

    private long id;
    private String title;
    private String artist;

    public LocalSong(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public long getID(){return id;}
    public String getTitleSong(){return title;}
    public String getArtist(){return artist;}


}
