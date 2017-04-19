package com.example.androidhive.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
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

import static com.example.androidhive.SlidingMenu.Audiolist;
import static com.example.androidhive.SlidingMenu.Imageadapter;
import static com.example.androidhive.SlidingMenu.Imagelist;
import static com.example.androidhive.SlidingMenu.Totallist;
import static com.example.androidhive.SlidingMenu.Videolist;

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
        View rView;
        switch (number){
            case 0:

                rView = inflater.inflate(R.layout.fragment_planet, container,false);
                Totallist = (ListView)rView.findViewById(R.id.list);

                SlidingMenu.Jparse.get_all_objects(SlidingMenu.user_id,3);

                Totallist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        item = ArticleLoaderTask.TotalList.get(position);
                        if (item.get(ArticleLoaderTask.KEY_TYPE).equals("mp3")) {
                            Intent intent = new Intent(getActivity(), StreamingMp3Player.class);
                            TextView Text = (TextView) view.findViewById(R.id.title);
                            String message = Text.getText().toString();
                            intent.putExtra("Title", message);
                            String obj_url = item.get(ArticleLoaderTask.KEY_OBJ_URL);
                            intent.putExtra("Url Object", obj_url);
                            startActivity(intent);
                            return;
                        } else if (item.get(ArticleLoaderTask.KEY_TYPE).equals("mp4")) {
                            Intent intent = new Intent(getActivity(), VideoPlayer.class);
                            intent.putExtra("url object", item.get(ArticleLoaderTask.KEY_OBJ_URL));
                            startActivity(intent);
                            return;
                        } else if (item.get(ArticleLoaderTask.KEY_TYPE).equals("jpg") || item.get(ArticleLoaderTask.KEY_TYPE).equals("png")) {

                                Intent intent = new Intent(getActivity(), ViewImage.class);
                                String obj_url = item.get(ArticleLoaderTask.KEY_OBJ_URL);
                                intent.putExtra("uri",obj_url);
                                startActivity(intent);
                        }
                    }

                });
                break;
            case 4:
                rView = inflater.inflate(R.layout.fragment_notifications,container,false);
                break;
            case 5:
                rView =  inflater.inflate(R.layout.fragment_settings,container,false);
                break;
            default:
                rView =  inflater.inflate(R.layout.fragment_home,container,false);
                break;
        }
        return rView;

    }

}
