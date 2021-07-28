package com.example.android.popularmovies.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.Model.Movie;

public class MainViewModel extends AndroidViewModel{

    LiveData<Movie[]> Movies ;
    public MainViewModel(@NonNull Application application) {

        super(application);

        MovieDB mDb = MovieDB.getsInstance(this.getApplication());
        Movies=mDb.movieDao().loadAllTasks();
    }

    public LiveData<Movie[]> getMovies() {
        return Movies;
    }
}
