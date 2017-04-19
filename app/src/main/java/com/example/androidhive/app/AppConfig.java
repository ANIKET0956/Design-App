package com.example.androidhive.app;

/**
 * Created by Prakhar Gupta on 3/28/2017.
 */

public class AppConfig {
    // Server user login url
    public static String SERVER_URL = "http://10.194.23.8/cms/";
    public static String URL_LOGIN = SERVER_URL + "login.php";

    // Server user register url
    public static String URL_REGISTER = SERVER_URL + "register.php";
    public static String URL_GETFAV = SERVER_URL + "getFav.php";
    public static String URL_ADDFAV = SERVER_URL + "addFav.php";
    public static String URL_GETPLAY = SERVER_URL + "getPlaylists.php";
    public static String URL_ADDPLAY = SERVER_URL + "addtoPlaylist.php";

    public static String UPLOAD_URL = SERVER_URL + "UploadToServer.php";
    public static String ADD_OBJECT_URL = SERVER_URL + "addObject.php";
    public static String ADD_TAG_URL = SERVER_URL + "addTag.php";
    public static String SEARCH_URL = SERVER_URL + "search.php";
    public static String DOWNLOAD_SOURCE_URL = SERVER_URL + "uploader/uploads/";

    public  static String URL_GETOBJECT = SERVER_URL + "getObjects.php";
}