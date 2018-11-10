package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.database.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<MovieObject> mMovieList;
    Context mContext;
    OnMovieClickListener mMovieClickListener;
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }


    @Override
    public int getItemCount() {
        if(mMovieList != null){
            return mMovieList.size();

        }
        return 0;
    }

    public interface OnMovieClickListener{
        public void onMovieClick(int position);
    }


    public MovieAdapter(Context context, OnMovieClickListener movieClickListener){
        mContext = context;
        mMovieClickListener = movieClickListener;
    }
    public void setMovieList(List<MovieObject> movieList){
        mMovieList = movieList;
        notifyDataSetChanged();
    }



    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            mMovieClickListener.onMovieClick(getAdapterPosition());
        }
        ImageView mPosterImageView;

        public MovieViewHolder(View view){
            super(view);
            mPosterImageView = view.findViewById(R.id.im_poster);
            view.setOnClickListener(this);

        }
        public void bind(int position){
            StringBuilder posterPath = new StringBuilder("http://image.tmdb.org/t/p/w185//");
            posterPath.append(mMovieList.get(position).getMPosterPath());
            Picasso.with(mContext).load(posterPath.toString()).into(mPosterImageView);

        }
    }
}
