package com.example.androidhive;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidhive.app.AppConfig;


public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext(),a);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.image); // thumb image
        final ImageView dot_image = (ImageView)vi.findViewById(R.id.three_dot);
        ImageView tag_image = (ImageView)vi.findViewById(R.id.image_hashtag);
        TextView tag_name = (TextView)vi.findViewById(R.id.tagname);

        final HashMap<String, String> song = data.get(position);

        
        // Setting all values in listview
        title.setText(song.get(CustomizedListView.KEY_TITLE));
        artist.setText("Unknown");
        duration.setText("5.00");

        String url_image = AppConfig.DOWNLOAD_SOURCE_URL+song.get(ArticleLoaderTask.KEY_OBJ_URL);
        thumb_image.setImageResource(R.drawable.par1);
        if(song.get(ArticleLoaderTask.KEY_TYPE).equals("mp3"))
        {   thumb_image.setImageResource(R.drawable.par1);tag_image.setImageResource(R.drawable.par1_1);
            tag_name.setText("Audio");}
        else if(song.get(ArticleLoaderTask.KEY_TYPE).equals("mp4")){thumb_image.setImageResource(R.drawable.par2);
            tag_image.setImageResource(R.drawable.par1_3);tag_name.setText("Video");}
        else if(song.get(ArticleLoaderTask.KEY_TYPE).equals("jpg") || song.get(ArticleLoaderTask.KEY_TYPE).equals("png")){
            imageLoader.DisplayImage(url_image,thumb_image);tag_image.setImageResource(R.drawable.par1_2);tag_name.setText("Image");
        }



        if(SlidingMenu.navItemIndex!=0)
        {
            tag_image.setVisibility(View.GONE);
            tag_name.setVisibility(View.GONE);
        }

        dot_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                android.widget.PopupMenu popup = new android.widget.PopupMenu(activity, dot_image);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(activity,"Marked Favourite",Toast.LENGTH_SHORT).show();
                        Log.d("Marking favourite: ", song.toString());
                        Log.d("favourite objid: ", song.get(ArticleLoaderTask.KEY_ID));
                        switch (item.getItemId()){
                            case R.id.one:
                                if(song.get(ArticleLoaderTask.KEY_TYPE).equals("mp3")) {
                                    Log.d("tag with purpose",song.get(ArticleLoaderTask.KEY_ID));
                                    SlidingMenu.Jparse.addFavorite(SlidingMenu.user_id, song.get(ArticleLoaderTask.KEY_ID));
                                    ArticleLoaderTask.AudioFavSongsList.add(song);
                                }
                                else if(song.get(ArticleLoaderTask.KEY_TYPE).equals("mp4")) {
                                    SlidingMenu.Jparse.addFavorite(SlidingMenu.user_id,song.get(ArticleLoaderTask.KEY_ID));
                                    ArticleLoaderTask.VideoFavSongsList.add(song);
                                }
                                else if(song.get(ArticleLoaderTask.KEY_TYPE).equals("jpg") || song.get(ArticleLoaderTask.KEY_TYPE).equals("png")) {
                                    SlidingMenu.Jparse.addFavorite(SlidingMenu.user_id,song.get(ArticleLoaderTask.KEY_ID));
                                    ArticleLoaderTask.ImageFavList.add(song);
                                    System.out.println(ArticleLoaderTask.ImageFavList.toString());
                                }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });

        return vi;
    }
}