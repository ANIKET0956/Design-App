package com.example.androidhive.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.androidhive.ArticleLoaderTask;
import com.example.androidhive.CustomizedListView;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.SlidingMenu;
import com.example.androidhive.StreamingMp3Player;
import com.example.androidhive.VideoPlayer;
import com.example.androidhive.ViewImage;
import com.example.androidhive.app.AppConfig;
import com.example.androidhive.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Aniket on 4/7/2017.
 */


public class ImageFavourite extends Fragment {



    public ImageFavourite() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_planet, null);
        SlidingMenu.ImageFavlist = (ListView) vi.findViewById(R.id.list);


        ArticleLoaderTask.ImageFavList.clear();

        getFavorites(SlidingMenu.user_id);


        SlidingMenu.ImageFavlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap<String, String> image = new HashMap<String, String>();
                image  = ArticleLoaderTask.ImageFavList.get(position);
                System.out.println("imagefavlist onclick: " + image.toString());
                Intent intent = new Intent(getActivity(), ViewImage.class);
                String obj_url = image.get(ArticleLoaderTask.KEY_OBJ_URL);
                intent.putExtra("uri",obj_url);
                startActivity(intent);
            }
        });

        return vi;
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
                        System.out.println("Favourites returned: "+ user_id + " : " + favs.toString() );
                        for(int i=0; i<favs.length(); i++){
                            JSONObject f = favs.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(ArticleLoaderTask.KEY_ID,Integer.toString(f.getInt("id")));
                            map.put(ArticleLoaderTask.KEY_TYPE,f.getString("type"));
                            map.put(ArticleLoaderTask.KEY_OBJ_URL,f.getString("uri"));
                            map.put(ArticleLoaderTask.KEY_TITLE,f.getString("uri").replaceFirst("[.][^.]+$", ""));
                            Log.d("statement",f.getString("id"));
                            if(f.getString("type").equals("jpg") || f.getString("type").equals("png"))ArticleLoaderTask.ImageFavList.add(map);
                        }
                        System.out.println("imagefavlist before onclicklistner: " + ArticleLoaderTask.ImageFavList.toString());
                        SlidingMenu.ImageFavadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.ImageFavList);
                        SlidingMenu.ImageFavlist.setAdapter(SlidingMenu.ImageFavadapter);

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
