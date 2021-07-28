package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Trailer;

import java.util.ArrayList;


public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.TrailerViewHolder> {
    Context context;
    ArrayList<Trailer> mTrailers;

    public YouTubeAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.mTrailers = trailers;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTrailer;
        ImageView mImageView;

        public TrailerViewHolder(View view) {
            super(view);

            mTrailer = (TextView) view.findViewById(R.id.textview_trailer);
            mImageView = (ImageView) view.findViewById(R.id.playvideo);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();

            Trailer trailer = mTrailers.get(i);

            String key = trailer.getKey();

            Context context = v.getContext();

            Intent intent = new Intent(context, YouTube.class);
            intent.putExtra("key", key);
            context.startActivity(intent);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.youtube_items, parent, false);

        TrailerViewHolder myViewHolder = new TrailerViewHolder(view);

        return myViewHolder;
    }

    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer db = mTrailers.get(position);

        holder.mTrailer.setText(db.getTrailer() + " " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}