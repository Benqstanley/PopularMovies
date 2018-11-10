package com.example.android.popularmovies.reviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    ArrayList<String> authors;
    ArrayList<String> contents;

    public void setAuthorsAndContents(ArrayList<String> authors, ArrayList<String> contents){
        this.authors = authors;
        this.contents = contents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(authors != null){
            return authors.size();
        }
        return 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView mAuthor;
        TextView mContent;

        public ReviewViewHolder(View view){
            super(view);
            mAuthor = view.findViewById(R.id.tv_author);
            mContent = view.findViewById(R.id.tv_content);
        }

        public void bind(int position){
            mAuthor.setText(authors.get(position));
            mContent.setText(contents.get(position));
        }

    }
}
