package com.example.android.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.DB.Executors;
import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.DB.MovieDB;
import com.example.android.popularmovies.Model.Trailer;
import com.squareup.picasso.Picasso;


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

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {


    public static Movie movie;
    public static YouTubeAdapter tubeAdapter;
    public static ArrayList<Trailer> trailers;
    @BindView(R.id.tv_overview)
    TextView tv_overview;

    @BindView(R.id.tv_original_title)
    TextView tv_title;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.iv_poster_detail)
    ImageView iv_image;

    @BindView(R.id.tv_rate)
    TextView tv_rate;
    @BindView(R.id.im_fav)
    ImageButton imageButton;

    MovieDB mDb;


    boolean isFavourite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Intent i = getIntent();
        movie = (Movie) i.getParcelableExtra("Movie");
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + movie.getPoster()).into(iv_image);
        tv_date.setText(movie.getDate());
        tv_rate.setText(movie.getRate());
        tv_title.setText(movie.getTitle());
        tv_overview.setText(movie.getOverview());
        mDb = MovieDB.getsInstance(getApplicationContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Movie CheckMovieInFav = mDb.movieDao().selectMovieByID(movie.getId());
                if (CheckMovieInFav != null) {
                    isFavourite = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageButton.setBackgroundColor(Color.RED);
                        }
                    });
                }

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Rev_Youtube);
        FetchTrailers fetchMovieTrailer = new FetchTrailers();

        trailers = new ArrayList<Trailer>();
        tubeAdapter = new YouTubeAdapter(this, trailers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tubeAdapter);

        fetchMovieTrailer.execute(Configuration.Movie_Trailer
                + movie.getId()
                + Configuration.TRAILER_PATH);

    }


    public void Show_Reviews(View view) {
        Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
        intent.putExtra("MOVIE_ID", movie.getId());
        startActivity(intent);

    }

    public void Favourite_movies(View view) {

        isFavourite = !isFavourite;


        if (isFavourite) {

            Executors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertTask(movie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageButton.setBackgroundColor(Color.RED);


                        }
                    });

                }
            });
            Toast.makeText(this, " Movie added to favourites", Toast.LENGTH_SHORT).show();
        } else {
            Executors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteTask(movie);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageButton.setBackgroundColor(Color.WHITE);

                        }
                    });
                }
            });
            Toast.makeText(this, "Movie removed from favourites", Toast.LENGTH_SHORT).show();
        }


    }


    public class FetchTrailers extends AsyncTask<String, Void, String> {
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
        protected void onPostExecute(String TrJson) {
            super.onPostExecute(TrJson);

            Trailer trailer;


            try {
                JSONObject tRoot = new JSONObject(TrJson);

                JSONArray jsonArray = tRoot.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);

                    String key = jsonobject.getString("key");

                    trailer = new Trailer();
                    trailer.setTrailer(getString(R.string.Trailer));
                    trailer.setKey(key);

                    trailers.add(trailer);
                }

                tubeAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
