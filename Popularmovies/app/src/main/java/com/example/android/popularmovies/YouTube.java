package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YouTube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView youTubePlayerView;
    int id = 0;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        key = bundle.getString("key");

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube);

        youTubePlayerView.initialize(Configuration.YOUTUBE_APIKEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        if (null == youTubePlayer)
            return;

        if (!wasRestored) {
            youTubePlayer.cueVideo(key);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }
}