package com.example.androidhive.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidhive.ArticleLoaderTask;
import com.example.androidhive.CustomizedListView;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.SlidingMenu;
import com.example.androidhive.StreamingMp3Player;

import java.util.HashMap;
import java.util.SimpleTimeZone;


import com.example.androidhive.ArticleLoaderTask;
import com.example.androidhive.ViewImage;

/**
 * Created by Aniket on 4/7/2017.
 */



public class ImageFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";

    public ImageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vi = inflater.inflate(R.layout.fragment_planet, null);
        SlidingMenu.Imagelist = (ListView) vi.findViewById(R.id.list);

        SlidingMenu.Jparse.get_all_objects(SlidingMenu.user_id,0);

        SlidingMenu.Imageadapter = new LazyAdapter(getActivity(), ArticleLoaderTask.ImageList);
        SlidingMenu.Imagelist.setAdapter(SlidingMenu.Imageadapter);

        Log.d("check in time",Integer.toString(SlidingMenu.Imagelist.getCount()));

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
        return  vi;
    }
}

