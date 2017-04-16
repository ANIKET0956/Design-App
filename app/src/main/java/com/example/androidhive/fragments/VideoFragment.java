package com.example.androidhive.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.androidhive.ArticleLoaderTask;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.SlidingMenu;
import com.example.androidhive.VideoPlayer;

import java.util.HashMap;

import static com.example.androidhive.SlidingMenu.Videoadapter;
import static com.example.androidhive.SlidingMenu.Videolist;


/**
 * Created by Aniket on 4/7/2017.
 */



public class VideoFragment extends Fragment {

    public VideoFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_planet, null);
        Videolist = (ListView) vi.findViewById(R.id.list);

        SlidingMenu.Jparse.get_all_objects(SlidingMenu.user_id,2);
        Videoadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.VideoSongsList);
        Videolist.setAdapter(Videoadapter);


        Videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

        return  vi;
    }


}