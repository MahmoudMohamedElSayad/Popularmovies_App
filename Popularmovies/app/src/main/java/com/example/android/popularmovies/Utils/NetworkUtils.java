package com.example.android.popularmovies.Utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private NetworkUtils(){

    }

    private static final String BASE_URL="https://api.themoviedb.org/3/movie/";
    private static final String API="api_key";
    private static final String API_KEY="6f520821cbe7d8a6623235903a2787d5";




    public static URL buildUri (String type){
        Uri uri = Uri.parse(BASE_URL+type).buildUpon().appendQueryParameter(API,API_KEY).build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public static String  ReadData (URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean s = scanner.hasNext();
            if (s) {
                return scanner.next();
            } else {
                return null;

            }


        }finally {
            httpURLConnection.disconnect();

        }
    }












}
