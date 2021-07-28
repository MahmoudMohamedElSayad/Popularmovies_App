package com.example.android.popularmovies.Utils;

import com.example.android.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParse {

    public static Movie[] DataParsed(String json) {
        Movie[] movie_data = null;


        try {


            JSONObject root = new JSONObject(json);
            JSONArray jsonArray = root.getJSONArray("results");

            movie_data = new Movie[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject movie = jsonArray.getJSONObject(i);
                int id = Integer.parseInt(movie.getString("id"));

                String vote_average = movie.getString("vote_average");
                String title = movie.getString("title");
                String poster_path = movie.getString("poster_path");

                String overview = movie.getString("overview");
                String release_date = movie.getString("release_date");



                movie_data[i] = new Movie(id, title, poster_path, release_date, vote_average, overview);
            }

            return movie_data;


        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }


    }


}
