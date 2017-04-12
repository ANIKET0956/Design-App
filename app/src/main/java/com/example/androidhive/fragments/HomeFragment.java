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
import com.example.androidhive.R;
import com.example.androidhive.StreamingMp3Player;

import java.util.HashMap;

/**
 * Created by Aniket on 4/7/2017.
 */


public class HomeFragment extends  Fragment {

    public static final String ARG_PLANET_NUMBER = "planet_number";

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        int number = getArguments().getInt(ARG_PLANET_NUMBER);
        switch (number){
            case 0:
                rootView = inflater.inflate(R.layout.fragment_home,container,false);
                break;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_notifications,container,false);
                break;
            case 4:
                rootView =  inflater.inflate(R.layout.fragment_settings,container,false);
                break;
            default:
                rootView =  inflater.inflate(R.layout.fragment_home,container,false);
                break;
        }
        return rootView;
    }
}