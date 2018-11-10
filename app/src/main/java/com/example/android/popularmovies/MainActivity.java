package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.popularmovies.Utilities.JSONUtils;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.example.android.popularmovies.database.FavoriteDatabase;
import com.example.android.popularmovies.database.MovieObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    TextView results;
    String DETAILS;
    private List<MovieObject> listOfMovies;
    MovieAdapter movieAdapter;
    Spinner orderBySpinner;
    int sortBy;
    public static Context mContext;
    final public static int SORT_BY_POPULAR = 0;
    final public static int SORT_BY_RATED = 1;
    final public static int SORT_BY_FAVORITE = 2;
    RecyclerView mMovieRecyclerView;
    Parcelable recyclerViewState;
    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        recyclerViewState = mMovieRecyclerView.getLayoutManager().onSaveInstanceState();
        intent.putExtra("recycler_view_state", recyclerViewState);
        intent.putStringArrayListExtra(DETAILS, listOfMovies.get(position).printDetails());
        intent.putExtra("sort_by", sortBy);
        startActivity(intent);
    }
    public static Context getContext(){
        return mContext;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sort_by", sortBy);
        recyclerViewState = mMovieRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("recycler_view_state", recyclerViewState);
    }


    public void grabScrollState(Bundle savedInstanceState, Intent intent) {
        if(savedInstanceState != null){
            recyclerViewState = savedInstanceState.getParcelable("recycler_view_state");
        }else if(intent.hasExtra("recycler_view_state")) {
            recyclerViewState = intent.getParcelableExtra("recycler_view_state");
            Log.e("Has extra", ""+ recyclerViewState);
        }
    }

    public void setScrollState() {
        mMovieRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        Log.e("recycler view state", " " + mMovieRecyclerView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        grabScrollState(savedInstanceState, intent);
        mContext = this;
        orderBySpinner = (Spinner) findViewById(R.id.sp_sort_by);
        final String[] orderByOptions = getResources().getStringArray(R.array.sortby_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortby_options, R.layout.support_simple_spinner_dropdown_item);

        orderBySpinner.setAdapter(adapter);
        orderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(orderByOptions[0].equals(orderBySpinner.getSelectedItem())){
                    new FetchMovieTask().execute(Integer.toString(NetworkUtils.POPULAR));
                    sortBy = SORT_BY_POPULAR;

                }else if(orderByOptions[1].equals(orderBySpinner.getSelectedItem())){
                    new FetchMovieTask().execute(Integer.toString(NetworkUtils.RATED));
                    sortBy = SORT_BY_RATED;

                }else if(orderByOptions[2].equals(orderBySpinner.getSelectedItem())){
                    sortBy = SORT_BY_FAVORITE;
                    setupViewModel();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (intent.hasExtra("sort_by")){

            sortBy = intent.getIntExtra("sort_by", 0);
            orderBySpinner.setSelection(sortBy);
        }else{
            sortBy = orderBySpinner.getSelectedItemPosition();
            FetchMovieTask fetchMovies = new FetchMovieTask();
            fetchMovies.execute(Integer.toString(NetworkUtils.POPULAR));
        }
        movieAdapter = new MovieAdapter(this, this);
        if(savedInstanceState != null) {
            if (savedInstanceState.getInt("sort_bY", -1) != -1) {
                sortBy = savedInstanceState.getInt("sort_by");
                orderBySpinner.setSelection(sortBy);
                if (sortBy == 0) {
                    new FetchMovieTask().execute(Integer.toString(NetworkUtils.POPULAR));
                } else if (sortBy == 1) {
                    new FetchMovieTask().execute(Integer.toString(NetworkUtils.RATED));
                } else if (sortBy == 2) {
                    setupViewModel();
                }
            }
        }


        DETAILS = getString(R.string.details);
        results = (TextView) findViewById(R.id.tv_results);
        mMovieRecyclerView =  (RecyclerView) findViewById(R.id.rv_movie_grid);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mMovieRecyclerView.setAdapter(movieAdapter);
        mMovieRecyclerView.setLayoutManager(mLayoutManager);

    }

    public void fetchFavoritesFromDatabase(){
        final FavoriteDatabase mDb = FavoriteDatabase.getInstance(getContext());
        LiveData<List<MovieObject>> favorites = mDb.favoriteDao().getFavorites();
        favorites.observe(this, new Observer<List<MovieObject>>() {
            @Override
            public void onChanged(@Nullable List<MovieObject> movieObjects) {
                movieAdapter.setMovieList(movieObjects);
            }
        });
    }
    public void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
         viewModel.getFavorites().observe(this, new Observer<List<MovieObject>>() {
            @Override
            public void onChanged(@Nullable List<MovieObject> favorites) {
                movieAdapter.setMovieList(favorites);
                listOfMovies = favorites;
            }
        });
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            int sortByType = Integer.parseInt(strings[0]);
            URL movieRequestUrl = NetworkUtils.buildUrl(sortByType);
            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                return jsonMovieResponse;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("onPostExecute", "Done Executing");
            if(s!=null){
                listOfMovies = JSONUtils.fetchMovieList(s);
                if(listOfMovies == null){
                    results.setText("Something Failed");
                    return;
                }else {
                    movieAdapter.setMovieList(listOfMovies);
                    setScrollState();
                    recyclerViewState = null;
                }
                results.setText("Size: " + listOfMovies.size());
            }else{
                results.setText("Something Failed");
            }

        }
    }
}
