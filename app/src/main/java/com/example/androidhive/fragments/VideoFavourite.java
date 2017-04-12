package com.example.androidhive.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.androidhive.ArticleLoaderTask;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.SlidingMenu;
import com.example.androidhive.VideoPlayer;
import com.example.androidhive.app.AppConfig;
import com.example.androidhive.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.androidhive.SlidingMenu.VideoFavadapter;
import static com.example.androidhive.SlidingMenu.VideoFavlist;
import static com.example.androidhive.SlidingMenu.Videoadapter;
import static com.example.androidhive.SlidingMenu.Videolist;

/**
 * Created by Aniket on 4/7/2017.
 */

public class VideoFavourite extends Fragment {

    public  VideoFavourite()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_planet, null);
        VideoFavlist = (ListView) vi.findViewById(R.id.list);



        ArticleLoaderTask.VideoFavSongsList.clear();
        getFavorites(SlidingMenu.user_id);

        VideoFavlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap<String, String> vsong = new HashMap<String, String>();
                vsong  = ArticleLoaderTask.VideoFavSongsList.get(position);

                Intent intent = new Intent(getActivity(),VideoPlayer.class);
                intent.putExtra("url object",vsong.get(ArticleLoaderTask.KEY_OBJ_URL));
                startActivity(intent);
            }
        });

        return  vi;
    }


    private void getFavorites(final String user_id){

        String tag_string_req = "req_getfav";

        StringRequest strReq;
        strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETFAV, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Favourite", "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray favs = jObj.getJSONArray("favorites");
                        for(int i=0; i<favs.length(); i++){
                            JSONObject f = favs.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(ArticleLoaderTask.KEY_ID,Integer.toString(f.getInt("id")));
                            map.put(ArticleLoaderTask.KEY_TYPE,f.getString("type"));
                            map.put(ArticleLoaderTask.KEY_OBJ_URL,f.getString("uri"));
                            map.put(ArticleLoaderTask.KEY_TITLE,f.getString("uri").replaceFirst("[.][^.]+$", ""));
                            Log.d("statement",f.getString("id"));
                            if(f.getString("type").equals("mp4"))ArticleLoaderTask.VideoFavSongsList.add(map);
                        }

                        VideoFavadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.VideoFavSongsList);
                        VideoFavlist.setAdapter(VideoFavadapter);


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
