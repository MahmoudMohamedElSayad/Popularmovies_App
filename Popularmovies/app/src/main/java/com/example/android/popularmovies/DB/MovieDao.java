package com.example.android.popularmovies.DB;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.Model.Movie;



@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie ")
    LiveData<Movie[]> loadAllTasks();

    @Query("select * from movie where ID = :id")
    Movie selectMovieByID(int id);

    @Insert
    void insertTask(Movie mMovie);


    @Delete
    void deleteTask(Movie mMovie);


}