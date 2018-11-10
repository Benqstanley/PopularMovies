/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.Utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String QUERY_PARAM_API_KEY = "api_key";

    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String PATH_POPULAR =
            "popular";
    private static final String PATH_TOP_RATED =
            "top_rated";
    private static final String PATH_TRAILERS = "videos";

    private static final String PATH_REVIEWS = "reviews";

    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";

    public static final int POPULAR = 1;
    public static final int RATED = 2;


    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param sortByType The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(int sortByType) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL);
        if(sortByType == POPULAR){
            builtUri = builtUri.buildUpon().appendPath(PATH_POPULAR).appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY).build();
        }else{
            builtUri = builtUri.buildUpon().appendPath(PATH_TOP_RATED).appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY).build();
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildTrailersURL(int id){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL);
        builtUri = builtUri.buildUpon().appendPath(Integer.toString(id)).appendPath(PATH_TRAILERS)
                .appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY).build();
        Log.e("BuildingTrailerURL", Integer.toString(id) + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewsURL(int id){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL);
        builtUri = builtUri.buildUpon().appendPath(Integer.toString(id)).appendPath(PATH_REVIEWS)
                .appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY).build();
        Log.e("BuildingTrailerURL", Integer.toString(id) + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {

                return scanner.next();
            } else {
                return null;
            }

        }finally {
            urlConnection.disconnect();
        }
    }
}