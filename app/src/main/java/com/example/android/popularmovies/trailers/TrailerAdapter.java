package com.example.android.popularmovies.trailers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;

import java.net.URL;
import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    ArrayList<String> trailerNames;
    ArrayList<String> trailerKeys;

    public void setTrailerNamesAndUrls(ArrayList<String> trailerNames, ArrayList<String> trailerKeys) {
        this.trailerNames = trailerNames;
        this.trailerKeys = trailerKeys;
        notifyDataSetChanged();
    }
    public TrailerAdapter(ArrayList<String> names, ArrayList<String> keys){
        this.trailerNames = names;
        this.trailerKeys = keys;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(trailerNames != null){
            return trailerNames.size();
        }
        return 0;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
        Context context = MainActivity.getContext();
        ImageView mPlayButton;
        TextView mTrailerNumber;

        public TrailerViewHolder(View view) {
            super(view);
            mPlayButton = view.findViewById(R.id.play_button);
            mTrailerNumber = view.findViewById(R.id.trailer_number);
            mTrailerNumber.setText("TEST");
            mPlayButton.setOnClickListener(this);

        }
        public void bind(final int position) {
            mTrailerNumber.setText(trailerNames.get(position));
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = trailerKeys.get(position);
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + id));
                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
        }
    }
}
