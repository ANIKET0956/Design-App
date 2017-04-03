package com.example.androidhive;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.androidhive.CustomizedListView.KEY_ID;
import static com.example.androidhive.CustomizedListView.KEY_SONG;
import static com.example.androidhive.R.id.list;

/**
 * Created by Aniket on 4/1/2017.
 */

public class ArticleLoaderTask extends AsyncTask<Void, Void, ArrayList<HashMap<String,String>>> {

    LazyAdapter adapter;
    Activity mActivity;


    static final String URL = "http://api.androidhive.info/music/music.xml";

    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";

    static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    public ArticleLoaderTask(Activity a) {

        this.mActivity = a;

    }

    @Override
    protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {

        Log.d("Print url",CustomizedListView.URL);


        ArrayList<HashMap<String, String>> songs = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        Document doc = parser.getDomElement(xml); // getting DOM element


        NodeList nl = doc.getElementsByTagName(KEY_SONG);
        // looping through all song nodes <song>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            map.put(KEY_ID, parser.getValue(e, KEY_ID));
            map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
            map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
            map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
            map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

            // adding HashList to ArrayList
            songs.add(map);
        }

        return  songs;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String,String>> result) {
        super.onPostExecute(result);
        for(int i=0;i<result.size();i++)
        {
            songsList.add(result.get(i));
        }
    }

}