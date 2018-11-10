package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.Utilities.JSONUtils;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.reviews.ReviewAdapter;

import java.net.URL;
import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ArrayList<String> authors;
    ArrayList<String> contents;
    ReviewAdapter reviewAdapter;
    ArrayList<String> details;
    Parcelable recyclerViewState;
    int sortBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Intent intent = getIntent();
        recyclerViewState = intent.getParcelableExtra("recycler_view_state");
        details = intent.getStringArrayListExtra("DETAILS");
        sortBy = intent.getIntExtra("sort_by", 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = findViewById(R.id.reviews_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        reviewAdapter = new ReviewAdapter();
        mRecyclerView.setAdapter(reviewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        int id = intent.getIntExtra("ID", -1);
        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask();
        fetchReviewsTask.execute(Integer.toString(id));
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        intent.putStringArrayListExtra("DETAILS", details);
        intent.putExtra("sort_by", sortBy);
        intent.putExtra("recycler_view_state", recyclerViewState);
        return intent;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("DETAILS", details);
        intent.putExtra("sort_by", sortBy);
        intent.putExtra("recycler_view_state", recyclerViewState);
        super.onBackPressed();
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            int movieId = Integer.parseInt(strings[0]);
            URL trailerRequestUrl = NetworkUtils.buildReviewsURL(movieId);
            try {
                String jsonTrailersResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailerRequestUrl);
                return jsonTrailersResponse;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                authors = JSONUtils.fetchReviewAuthors(s);
                contents = JSONUtils.fetchReviewContents(s);
                if (authors.size() == 0) {
                    return;
                } else {
                    reviewAdapter.setAuthorsAndContents(authors, contents);
                }
            }
        }
    }
}
