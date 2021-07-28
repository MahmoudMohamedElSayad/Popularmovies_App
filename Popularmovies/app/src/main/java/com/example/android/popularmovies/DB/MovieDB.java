package com.example.android.popularmovies.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.Model.Movie;

@Database(entities = {Movie.class},version = 1,exportSchema = false)

public abstract class MovieDB extends RoomDatabase {

    private static final String LOG_TAG = MovieDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "moviedb";
    private static MovieDB mInstance;


    public static MovieDB getsInstance (Context context)
    {
        if (mInstance == null)
        {
        synchronized (LOCK){
            Log.d(LOG_TAG, "Creating new database instance");
            mInstance = Room.databaseBuilder(context.getApplicationContext(),MovieDB.class,MovieDB.DATABASE_NAME).build();
        }

        }
        Log.d(LOG_TAG, "Getting the database instance");
        return mInstance;
    }
    public abstract MovieDao movieDao();

}
