package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmovies.Model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    RecyclerView reviewRecyclerView;
    public static ReviewAdapter rAdapter;
    public static ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        reviewRecyclerView = (RecyclerView) findViewById(R.id.Rec_Reviews);

        FetchReviews fetchMovieReview = new FetchReviews();
        reviews = new ArrayList<Review>();
        rAdapter = new ReviewAdapter(reviews);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        reviewRecyclerView.setLayoutManager(layoutManager2);
        reviewRecyclerView.setAdapter(rAdapter);
        int id = getIntent().getExtras().getInt("MOVIE_ID");


        fetchMovieReview.execute(Configuration.Movie_Review
                + Integer.toString(id)
                + Configuration.Review_PATH);

    }

    public class FetchReviews extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String RevJson) {
            super.onPostExecute(RevJson);
            try {
                JSONObject RRoot = new JSONObject(RevJson);

                JSONArray jsonArray = RRoot.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    String content = object.getString("content");
                    String author = object.getString("author");

                    Review review = new Review();
                    review.setContent(content);
                    review.setAuthor(author);

                    reviews.add(review);
                }

                rAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
}
