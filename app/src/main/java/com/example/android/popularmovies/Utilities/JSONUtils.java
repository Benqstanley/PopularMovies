package com.example.android.popularmovies.Utilities;

import android.net.Uri;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.database.MovieObject;
import com.example.android.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class JSONUtils {


    private static String mTitle = MainActivity.getContext().getString(R.string.title_json);
    private static String mPosterPath = MainActivity.getContext().getString(R.string.poster_path_json);
    private static String mPlotSynopsis = MainActivity.getContext().getString(R.string.plot_synopsis_json);
    private static String mVoteAverage = MainActivity.getContext().getString(R.string.vote_average_json);
    private static String mReleaseDate = MainActivity.getContext().getString(R.string.release_date_json);
    private static String mKey = MainActivity.getContext().getString(R.string.key);
    private static String mId = MainActivity.getContext().getString(R.string.id_json);
    private static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";
    public static MovieObject createMovieObject(JSONObject JSONMovie){
        try {
            String title = JSONMovie.getString(mTitle);
            String posterPath = JSONMovie.getString(mPosterPath);
            String plotSynopsis = JSONMovie.getString(mPlotSynopsis);
            String voteAverage = JSONMovie.getString(mVoteAverage);
            String releaseDate = JSONMovie.getString(mReleaseDate);
            int id = JSONMovie.getInt(mId);
            return new MovieObject(title, posterPath, plotSynopsis, voteAverage, releaseDate, id);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<MovieObject> fetchMovieList(String JSONMovieListString){
        ArrayList<MovieObject> movieList = new ArrayList<>();
        try {
            JSONArray JSONMovieArray = (new JSONObject(JSONMovieListString)).getJSONArray("results");
            int length = JSONMovieArray.length();
            for(int k = 0; k < length; k++){
                JSONObject JSONMovie = new JSONObject(JSONMovieArray.get(k).toString());
                MovieObject movie = createMovieObject(JSONMovie);
                if(movie != null){
                    movieList.add(movie);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return movieList;
    }

    public static ArrayList<String> fetchTrailerNamesList(String JSONTrailerListString){
        ArrayList<String> names = new ArrayList<>();
        try{
            JSONArray JSONTrailerArray = (new JSONObject(JSONTrailerListString))
                    .getJSONArray("results");
            int length = JSONTrailerArray.length();
            for(int k = 0; k < length; k++){
                JSONObject JSONTrailer = new JSONObject(JSONTrailerArray.get(k).toString());
                String name = createTrailerName(JSONTrailer);
                names.add(name);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return names;
    }
    public static ArrayList<String> fetchTrailerKeys(String JSONTrailerListString){
        ArrayList<String> keys = new ArrayList<>();
        try{
            JSONArray JSONTrailerArray = (new JSONObject(JSONTrailerListString))
                    .getJSONArray("results");
            int length = JSONTrailerArray.length();
            for(int k = 0; k < length; k++){
                JSONObject JSONTrailer = new JSONObject(JSONTrailerArray.get(k).toString());
                String key = createTrailerKey(JSONTrailer);
                keys.add(key);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return keys;
    }

    public static ArrayList<String> fetchReviewAuthors(String JSONReviewListString){
        ArrayList<String> authors = new ArrayList<>();
        try{
            JSONArray JSONTrailerArray = (new JSONObject(JSONReviewListString))
                    .getJSONArray("results");
            int length = JSONTrailerArray.length();
            for(int k = 0; k < length; k++){
                JSONObject JSONReview = new JSONObject(JSONTrailerArray.get(k).toString());
                String author = JSONReview.getString("author");
                authors.add(author);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return authors;
    }
    public static ArrayList<String> fetchReviewContents(String JSONReviewListString){
        ArrayList<String> contents = new ArrayList<>();
        try{
            JSONArray JSONTrailerArray = (new JSONObject(JSONReviewListString))
                    .getJSONArray("results");
            int length = JSONTrailerArray.length();
            for(int k = 0; k < length; k++){
                JSONObject JSONReview = new JSONObject(JSONTrailerArray.get(k).toString());
                String content = JSONReview.getString("content");
                contents.add(content);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return contents;
    }

    public static String createTrailerName(JSONObject JSONTrailer){
        try {
            return JSONTrailer.getString("name");
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String createTrailerKey(JSONObject JSONTrailer){
        try {
            return JSONTrailer.getString(mKey);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}
