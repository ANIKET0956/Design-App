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
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.SlidingMenu;
import com.example.androidhive.StreamingMp3Player;

import java.util.HashMap;

/**
 * Created by Aniket on 4/7/2017.
 */



public class AudioFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public AudioFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_planet, null);
        SlidingMenu.Audiolist = (ListView) vi.findViewById(R.id.list);

        SlidingMenu.Jparse.get_all_objects(SlidingMenu.user_id,1);

        SlidingMenu.Audioadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.AudioSongsList);
        SlidingMenu.Audiolist.setAdapter(SlidingMenu.Audioadapter);

        Log.d("check in time",Integer.toString(SlidingMenu.Audiolist.getCount()));

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
        return  vi;
    }
}

