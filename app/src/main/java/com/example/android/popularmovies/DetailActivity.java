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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies.Utilities.JSONUtils;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.database.FavoriteDatabase;
import com.example.android.popularmovies.database.MovieObject;
import com.example.android.popularmovies.trailers.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ImageView mPosterImageView;
    Button mFavorite;
    Button mUnfavorite;
    FavoriteDatabase mDatabase;
    MovieObject movieObject;
    ArrayList<String> details;
    RecyclerView mRecyclerView;
    ArrayList<String> trailerNames = new ArrayList<>();
    ArrayList<String> trailerKeys = new ArrayList<>();
    TrailerAdapter trailerAdapter;
    Parcelable recyclerViewState;
    int sortBy;
    int id;
    TextView mReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);
        sortBy = getIntent().getIntExtra("sort_by", 0);
        mReviews = findViewById(R.id.reviews);
        mFavorite = (Button) findViewById(R.id.favorite);
        mUnfavorite = (Button) findViewById(R.id.unfavorite);
        mDatabase = FavoriteDatabase.getInstance(this);
        Intent intent = getIntent();
        recyclerViewState = intent.getParcelableExtra("recycler_view_state");
        Log.e("Detail Activity", "" + recyclerViewState);
        details = intent.getStringArrayListExtra(getString(R.string.details));
        setupRecyclerView();
        populateUI();
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.favoriteDao().insertFavorite(movieObject);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showCorrectButton(true);
                            }
                        });
                    }

                }
                )).start();

            }
        });
        mUnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.favoriteDao().deleteFavorite(movieObject);
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              showCorrectButton(false);
                                          }
                                      });
                    }
                })).start();
            }
        });
        mReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
                intent.putExtra("ID", id);
                intent.putStringArrayListExtra("DETAILS", details);
                intent.putExtra("sort_by", sortBy);
                intent.putExtra("recycler_view_state", recyclerViewState);
                startActivity(intent);
            }
        });
    }
    public void showCorrectButton(boolean favorite){
        if(favorite){
            mUnfavorite.setVisibility(View.VISIBLE);
            mFavorite.setVisibility(View.GONE);
        }else{
            mFavorite.setVisibility(View.VISIBLE);
            mUnfavorite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("sort_by", sortBy);
        intent.putExtra("recycler_view_state", recyclerViewState);
        super.onBackPressed();
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        intent.putExtra("sort_by", sortBy);
        intent.putExtra("recycler_view_state", recyclerViewState);
        return intent;
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        intent.putExtra("sort_by", sortBy);
        intent.putExtra("recycler_view_state", recyclerViewState);
        return intent;
    }

    public void populateTrailerUrlsList(){
        FetchTrailersTask fetchTrailersTask = new FetchTrailersTask();
        fetchTrailersTask.execute(Integer.toString(id));
    }
    public void setupRecyclerView(){
        mRecyclerView = findViewById(R.id.trailer_recycler);
        id = Integer.parseInt(details.get(5));
        populateTrailerUrlsList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        trailerAdapter = new TrailerAdapter(trailerNames, trailerKeys);
        mRecyclerView.setAdapter(trailerAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void populateUI(){
        ((TextView) findViewById(R.id.tv_title)).setText(details.get(1));
        ((TextView) findViewById(R.id.tv_vote_average)).setText(details.get(2) + "/10");
        ((TextView) findViewById(R.id.tv_release_date)).setText(details.get(3));
        ((TextView) findViewById(R.id.tv_plot_synopsis)).setText(details.get(4));

        StringBuilder posterPath = new StringBuilder("http://image.tmdb.org/t/p/w185//");
        posterPath.append(details.get(0));
        mPosterImageView = (ImageView) findViewById(R.id.im_poster_details);
        Picasso.with(getApplicationContext()).load(posterPath.toString()).into(mPosterImageView);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                movieObject = mDatabase.favoriteDao().returnFavoriteById(id);
                final boolean favorite = movieObject != null;
                if(!favorite){
                    movieObject = new MovieObject(details);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCorrectButton(favorite);
                    }
                });
            }
        });
        thread.start();
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            int movieId = Integer.parseInt(strings[0]);
            URL trailerRequestUrl = NetworkUtils.buildTrailersURL(movieId);
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
            if(s!=null){
                trailerNames = JSONUtils.fetchTrailerNamesList(s);
                trailerKeys = JSONUtils.fetchTrailerKeys(s);
                if(trailerNames.size() == 0) {
                    ((TextView) findViewById(R.id.trailers)).setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    return;
                }else {
                    trailerAdapter.setTrailerNamesAndUrls(trailerNames, trailerKeys);
                    if(trailerNames.size() < 3){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.weight = 0f;
                        ((View) mRecyclerView).setLayoutParams(params);
                    }
                }
            }

        }
    }

}
