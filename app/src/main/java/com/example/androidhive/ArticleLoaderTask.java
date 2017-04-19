package com.example.androidhive;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

    static public final String KEY_SONG = "song"; // parent node
    static public final String KEY_ID = "id";
    static public final String KEY_TITLE = "title";
    static public final String KEY_LABEL = "label";
    static public final String KEY_ARTIST = "artist";
    static public  final String KEY_DURATION = "duration";
    static public final String KEY_THUMB_URL = "thumb_url";
    static public  final String KEY_TYPE = "type";

    public static final String KEY_OBJ_URL = "url_file";

    public static ArrayList<HashMap<String, String>> ImageList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> ImageFavList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> AudioSongsList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> VideoSongsList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> AudioFavSongsList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> VideoFavSongsList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> TotalList = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> TrendingList = new ArrayList<HashMap<String, String>>();


    public ArticleLoaderTask(Activity a) {

        this.mActivity = a;

    }

    @Override
    protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {

        ArrayList<HashMap<String, String>> total_objects = new ArrayList<HashMap<String, String>>();

        for(int i=0;i<SlidingMenu.favs.length();i++)
        {
            JSONObject f;
            try{
                f = SlidingMenu.favs.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(KEY_ID,Integer.toString(f.getInt("id")));
                map.put(KEY_TYPE,f.getString("type"));
                map.put(KEY_OBJ_URL,f.getString("uri"));
                map.put(KEY_TITLE,f.getString("uri").replaceFirst("[.][^.]+$", ""));
                map.put(KEY_LABEL,f.getString("label"));
                total_objects.add(map);
            }
            catch (JSONException e) {
                Log.d("exception","raise exception");
            }
        }

        return  total_objects;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String,String>> result) {
        super.onPostExecute(result);
        for(int i=0;i<result.size();i++)
        {
            if(result.get(i).get(KEY_TYPE).equals("mp3")){

                Log.d("msg me","i am here");
                AudioSongsList.add(result.get(i));
            }
        }

    }

}