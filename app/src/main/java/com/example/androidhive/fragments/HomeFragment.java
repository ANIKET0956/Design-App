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
import android.widget.TextView;

import com.example.androidhive.ArticleLoaderTask;
import com.example.androidhive.CustomizedListView;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.SlidingMenu;
import com.example.androidhive.StreamingMp3Player;
import com.example.androidhive.VideoPlayer;
import com.example.androidhive.ViewImage;

import java.util.HashMap;

/**
 * Created by Aniket on 4/7/2017.
 */


public class HomeFragment extends  Fragment {

    public static final String ARG_PLANET_NUMBER = "planet_number";
    private static View rootView;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int number = getArguments().getInt(ARG_PLANET_NUMBER);
        switch (number){
            case 0:
                rootView = inflater.inflate(R.layout.fragment_home,container,false);
                SlidingMenu.Imagelist = (ListView) rootView.findViewById(R.id.imagelist);
                SlidingMenu.Audiolist = (ListView) rootView.findViewById(R.id.audiolist);
                SlidingMenu.Videolist = (ListView) rootView.findViewById(R.id.videolist);

                SlidingMenu.Jparse.get_all_objects(SlidingMenu.user_id,3);

                SlidingMenu.Imageadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.ImageList);
                SlidingMenu.Imagelist.setAdapter(SlidingMenu.Imageadapter);

                SlidingMenu.Audioadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.AudioSongsList);
                SlidingMenu.Audiolist.setAdapter(SlidingMenu.Audioadapter);

                SlidingMenu.Videoadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.VideoSongsList);
                SlidingMenu.Videolist.setAdapter(SlidingMenu.Videoadapter);

                Log.d("check in time",Integer.toString(SlidingMenu.Imagelist.getCount()) + " , " + Integer.toString(SlidingMenu.Audiolist.getCount()) + " , " + Integer.toString(SlidingMenu.Videolist.getCount()));

                SlidingMenu.Imagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        HashMap<String, String> image = new HashMap<String, String>();
                        image  = ArticleLoaderTask.ImageList.get(position);
                        System.out.println("imagelist onclick:");
                        System.out.println(image.toString());
                        Intent intent = new Intent(getActivity(), ViewImage.class);
                        String obj_url = image.get(ArticleLoaderTask.KEY_OBJ_URL);
                        intent.putExtra("uri",obj_url);
                        startActivity(intent);
                    }
                });
                SlidingMenu.Audiolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        HashMap<String, String> song = new HashMap<String, String>();
                        song  = ArticleLoaderTask.AudioSongsList.get(position);
                        Intent intent = new Intent(getActivity(), StreamingMp3Player.class);
                        TextView Text = (TextView) view.findViewById(R.id.title);
                        String message = Text.getText().toString();
                        intent.putExtra("Title", message);
                        String obj_url = song.get(ArticleLoaderTask.KEY_OBJ_URL);
                        intent.putExtra("Url Object",obj_url);
                        startActivity(intent);
                    }
                });
                SlidingMenu.Videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        HashMap<String, String> vsong = new HashMap<String, String>();
                        vsong  = ArticleLoaderTask.VideoSongsList.get(position);

                        Intent intent = new Intent(getActivity(),VideoPlayer.class);
                        intent.putExtra("url object",vsong.get(ArticleLoaderTask.KEY_OBJ_URL));
                        startActivity(intent);
                    }
                });
                break;
            case 4:
                rootView = inflater.inflate(R.layout.fragment_notifications,container,false);
                break;
            case 5:
                rootView =  inflater.inflate(R.layout.fragment_settings,container,false);
                break;
            default:
                rootView =  inflater.inflate(R.layout.fragment_home,container,false);
                break;
        }
        return rootView;
    }
}