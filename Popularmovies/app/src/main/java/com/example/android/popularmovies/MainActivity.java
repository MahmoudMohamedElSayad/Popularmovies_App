package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.DB.MainViewModel;
import com.example.android.popularmovies.Model.Movie;
import com.example.android.popularmovies.Utils.JsonParse;
import com.example.android.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickHandler {
    GridLayoutManager gridLayoutManager;
    static MovieAdapter adapter;
    static RecyclerView recyclerView;
    static Movie[] movies;
    static String type;
    SharedPreferences sharedPreferences;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITE = "favorite";

    static TextView tv_error;
    int state;
    String keystate = "SCROLL_POSITION";
    public static int prevstate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_error = (TextView) findViewById(R.id.error);
        recyclerView = (RecyclerView) findViewById(R.id.recview);

        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, 2);


        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MovieAdapter(this);

        recyclerView.setAdapter(adapter);
        if (savedInstanceState != null) {
            Log.d("kkey", "valll");

            if (savedInstanceState.containsKey(keystate)) {
                prevstate = savedInstanceState.getInt(keystate);
                Toast.makeText(this, "Position: " + savedInstanceState.getInt(keystate), Toast.LENGTH_SHORT).show();
                Log.e("state", prevstate + "");

            }

        }


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        type = sharedPreferences.getString("popular", POPULAR);

        loadMovieData();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        state = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

        if (state == -1) {
            state = gridLayoutManager.findLastVisibleItemPosition();
        }

        Log.e("mY_sTATE", state + "");
        outState.putInt(keystate, state);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        movies = (Movie[]) savedInstanceState.getParcelableArray("DataSaved");

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("DataSaved")) {
                movies = (Movie[]) savedInstanceState.getParcelableArray("DataSaved");
            }
        }

        super.onRestoreInstanceState(savedInstanceState);

    }

    public String getType() {
        return this.type;
    }

    public static void setType(String type) {
        MainActivity.type = type;
    }

    public static void loadMovieData() {

        tv_error.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        process p = new process();
        p.execute(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();

        switch (menu_id) {
            case R.id.m_mostPopular:
                Toast.makeText(this, "popular", Toast.LENGTH_SHORT).show();
                if (type.equals(POPULAR) != true) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("popular", POPULAR);
                    editor.commit();
                    type = POPULAR;
                    loadMovieData();
                }
                type = POPULAR;
                loadMovieData();
                return true;

            case R.id.m_topRated:
                Toast.makeText(this, "top Rated", Toast.LENGTH_SHORT).show();
                if (type.equals(TOP_RATED) != true) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("top Rated", TOP_RATED);
                    editor.commit();
                    type = TOP_RATED;
                    loadMovieData();
                }
                type = TOP_RATED;
                loadMovieData();
                return true;

            case R.id.m_favorite:
                Toast.makeText(this, "Favorites ", Toast.LENGTH_SHORT);

                loadFavourites();

                return true;


        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void OnClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);

    }

    public static class process extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String mType = params[0];

            URL url = NetworkUtils.buildUri(mType);
            try {
                String response = NetworkUtils.ReadData(url);
                movies = JsonParse.DataParsed(response);
                return movies;


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            if (movieData != null) {
                movies = movieData;
                adapter.setMovieData(movies);

                if (prevstate >= 0) {
                    recyclerView.scrollToPosition(prevstate);
                } else {
                    recyclerView.scrollToPosition(0);
                }
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                tv_error.setVisibility(View.VISIBLE);
            }

        }
    }

    public void loadFavourites() {


        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);

        model.getMovies().observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] movies) {
                Log.d("Ghon", "Updating list of tasks from LiveData in ViewModel");
                adapter.setMovieData(movies);

            }
        });


    }

}
