

package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviews;

    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView Author;
        TextView Content;


        public ReviewViewHolder(View view) {
            super(view);
            Author = (TextView) itemView.findViewById(R.id.Rev_author);

            Content = (TextView) view.findViewById(R.id.Rev_content);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.review_items, parent, false);

        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder viewHolder, int position) {
        Review review = reviews.get(position);

        viewHolder.Author.setText(review.getAuthor());

        viewHolder.Content.setText(review.getContent());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}


