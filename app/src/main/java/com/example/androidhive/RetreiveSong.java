package com.example.androidhive;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Aniket on 3/31/2017.
 */

public class RetreiveSong extends Activity{



    public RetreiveSong()
    {
    }

    public ArrayList<LocalSong> getSongList() {

        ArrayList<LocalSong> songList = new ArrayList<>();

        ContentResolver musicResolver = getApplicationContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new LocalSong(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

        return songList;
    }

}
