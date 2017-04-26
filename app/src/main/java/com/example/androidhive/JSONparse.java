package com.example.androidhive;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.androidhive.app.AppConfig;
import com.example.androidhive.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.androidhive.ArticleLoaderTask.AudioSongsList;
import static com.example.androidhive.ArticleLoaderTask.ImageList;
import static com.example.androidhive.ArticleLoaderTask.KEY_ID;
import static com.example.androidhive.ArticleLoaderTask.KEY_OBJ_URL;
import static com.example.androidhive.ArticleLoaderTask.KEY_TITLE;
import static com.example.androidhive.ArticleLoaderTask.KEY_LABEL;
import static com.example.androidhive.ArticleLoaderTask.KEY_TYPE;
import static com.example.androidhive.ArticleLoaderTask.TrendingList;
import static com.example.androidhive.ArticleLoaderTask.VideoSongsList;
import static com.example.androidhive.SlidingMenu.Videoadapter;
import static com.example.androidhive.SlidingMenu.Videolist;

/**
 * Created by Aniket on 4/11/2017.
 */

public class JSONparse {

    public int FIXED_SIZE = 10;
    Activity mActivity;
    public JSONparse(Activity a)
    {
        mActivity = a;
    }

    public void get_all_objects(final String user_id,final int index){

        String tag_string_req = "req_get_object";

        if(index==0) ArticleLoaderTask.ImageList.clear();
        else if(index==1)ArticleLoaderTask.AudioSongsList.clear();
        else if(index==2)ArticleLoaderTask.VideoSongsList.clear();
        else if(index==3){ArticleLoaderTask.TotalList.clear();TrendingList.clear();}

        final ArrayList<HashMap<String, String>> total_objects = new ArrayList<HashMap<String, String>>();

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETOBJECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Sliding Menu", "Register Response: " + response.toString());

                try {
                    Log.d("starting response","check this");
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                       SlidingMenu.favs = jObj.getJSONArray("objects");
                        for(int i=0;i<SlidingMenu.favs.length();i++)
                        {
                            JSONObject f;
                            try{
                                f = SlidingMenu.favs.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(KEY_ID,Integer.toString(f.getInt("id")));
                                map.put(KEY_TYPE,f.getString("type"));
                                map.put(KEY_OBJ_URL,f.getString("uri"));
                                map.put(KEY_LABEL,f.getString("label"));
                                map.put(KEY_TITLE,f.getString("uri").replaceFirst("[.][^.]+$", ""));
                                total_objects.add(map);
                            }
                            catch (JSONException e) {
                                Log.d("exception","raise exception");
                            }
                        }
                        for(int i=0;i<total_objects.size();i++)
                        {
                            if(total_objects.get(i).get(KEY_TYPE).equals("jpg") || total_objects.get(i).get(KEY_TYPE).equals("png")){
                                ImageList.add(total_objects.get(i));
                            }
                            if(total_objects.get(i).get(KEY_TYPE).equals("mp3")){
                                AudioSongsList.add(total_objects.get(i));
                            }
                            if(total_objects.get(i).get(KEY_TYPE).equals("mp4")){
                                VideoSongsList.add(total_objects.get(i));
                            }
                            ArticleLoaderTask.TotalList.add(total_objects.get(i));
                        }

                        long seed = System.nanoTime();
                        Collections.shuffle(ArticleLoaderTask.TotalList, new Random(seed));

                        for(int i=0;i<FIXED_SIZE;i++) {
                            TrendingList.add(ArticleLoaderTask.TotalList.get(i));
                        }

                        switch(index)
                        {
                            case 0:
                                SlidingMenu.Imageadapter = new LazyAdapter(mActivity, ArticleLoaderTask.ImageList);
                                SlidingMenu.Imagelist.setAdapter(SlidingMenu.Imageadapter);
                                break;
                            case 1:
                                SlidingMenu.Audioadapter = new LazyAdapter(mActivity, ArticleLoaderTask.AudioSongsList);
                                SlidingMenu.Audiolist.setAdapter(SlidingMenu.Audioadapter);
                                break;
                            case 2:
                                SlidingMenu.Videoadapter = new LazyAdapter(mActivity, ArticleLoaderTask.VideoSongsList);
                                SlidingMenu.Videolist.setAdapter(SlidingMenu.Videoadapter);
                                break;
                            case 3:
                                Log.d("size totallist",Integer.toString(ArticleLoaderTask.TrendingList.size()));
                                SlidingMenu.Totaladapter = new LazyAdapter(mActivity, ArticleLoaderTask.TrendingList);
                                SlidingMenu.Totallist.setAdapter(SlidingMenu.Totaladapter);
                                break;
                        }


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mActivity,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("user id",user_id);
                params.put("user_id", user_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void addFavorite(final String user_id, final String object_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_addfav";

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADDFAV, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Sliding Menu", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mActivity,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Registration Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("object_id", object_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void removeFavorite(final String user_id, final String object_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_removefav";

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REMFAV, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("JSONParse: ", "Remove Favourite: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(mActivity,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Tag", "Remove Favorite Error: " + error.getMessage());
                Toast.makeText(mActivity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("object_id", object_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            Log.d("count element",Integer.toString(mListAdapter.getCount()));
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }


}
