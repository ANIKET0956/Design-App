package com.example.androidhive;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidhive.fragments.AudioFavourite;
import com.example.androidhive.fragments.AudioFragment;
import com.example.androidhive.fragments.HomeFavourite;
import com.example.androidhive.fragments.HomeFragment;
import com.example.androidhive.fragments.ImageFavourite;
import com.example.androidhive.fragments.ImageFragment;
import com.example.androidhive.fragments.VideoFavourite;
import com.example.androidhive.fragments.VideoFragment;
import com.example.androidhive.helper.SQLiteHandler;
import com.example.androidhive.helper.SessionManager;


import org.json.JSONArray;

import java.util.HashMap;


public class
SlidingMenu extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    static Context mContext;
    static Activity mActivity;

    public static ListView Audiolist,AudioFavlist,Videolist,VideoFavlist, Imagelist, ImageFavlist,Totallist;
    public static LazyAdapter Audioadapter,AudioFavadapter,Videoadapter,VideoFavadapter, Imageadapter, ImageFavadapter,Totaladapter;

    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_IMAGE = "photos";
    private static final String TAG_AUDIO = "photos";
    private static final String TAG_VIDEO = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;
    static final String KEY_TYPE = "type";

    public static JSONArray favs;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";


    private SQLiteHandler db;
    private SessionManager session;
    public static String user_id;
    public static String user_name;
    public static JSONparse Jparse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();
        mActivity = this;
        mContext = getBaseContext();


        Jparse = new JSONparse(mActivity);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        HashMap<String, String> user = db.getUserDetails();
        user_id = user.get("dbid");
        user_name = user.get("username");

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */

    private void loadNavHeader() {
        // name, website
        txtName.setText(user_name);
        txtWebsite.setText("Content Mangement System");

        // loading header background image

        /*
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);
           */
       // imgNavHeaderBg.setImageResource(R.drawable.nav_header_img);

        imgProfile.setImageResource(R.drawable.batman);
        // showing dot next to notifications label
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        for(int i=0;i<6;i++) {
            navigationView.getMenu().getItem(i).setActionView(null);
        }
        navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
    }

    // show or hide the fab
    private void toggleFab() {
            fab.hide();
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();


        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }


        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments

                Fragment fragment = getHomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment()
    {
        Bundle args = new Bundle();
        Fragment fragment;

        switch (navItemIndex){
            case 0:
                fragment =  new HomeFragment();
                args.putInt(HomeFragment.ARG_PLANET_NUMBER, navItemIndex);
                fragment.setArguments(args);
                break;
            case 1:
                fragment =  new ImageFragment();
                break;
            case 2:
                fragment =  new AudioFragment();
                break;
            case 3:
                fragment =  new VideoFragment();
                break;
            case 4:
                fragment =  new HomeFragment();
                args.putInt(HomeFragment.ARG_PLANET_NUMBER, navItemIndex);
                fragment.setArguments(args);
                break;
            case 5:
                fragment =  new HomeFragment();
                args.putInt(HomeFragment.ARG_PLANET_NUMBER, navItemIndex);
                fragment.setArguments(args);
                break;
            default:
                fragment =  new HomeFragment();
                args.putInt(HomeFragment.ARG_PLANET_NUMBER, navItemIndex);
                fragment.setArguments(args);
                break;
        }
        return  fragment;
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_image:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_IMAGE;
                        break;
                    case R.id.nav_audio:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_AUDIO;
                        break;
                    case R.id.nav_video:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_VIDEO;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
            return  true;
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 4) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        else if(navItemIndex < 4){
            getMenuInflater().inflate(R.menu.favourite, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            logoutUser();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.favouite_list) {
            Fragment fragment = null;
            switch (navItemIndex) {
                case 0:
                    fragment = new HomeFavourite();
                    break;
                case 1:
                    fragment = new ImageFavourite();
                    break;
                case 2:
                    fragment = new AudioFavourite();
                    break;
                case 3 :
                    fragment =  new VideoFavourite();
                    break;
            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

        }

        if( id == R.id.all_list)
        {   Fragment fragment = null;
            switch (navItemIndex){
                case 0:
                    Bundle args = new Bundle();
                    fragment = new HomeFragment();
                    args.putInt(HomeFragment.ARG_PLANET_NUMBER, navItemIndex);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new ImageFragment();
                    break;
                case 2:
                    fragment = new AudioFragment();
                    break;
                case 3:
                    fragment = new VideoFragment();
                    break;
            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        if( id == R.id.action_websearch)
        {
            /*
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            */
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser()
    {
        session.setLogin(false);
        db.deleteUsers();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}