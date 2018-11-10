package com.example.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.popularmovies.database.FavoriteDatabase;
import com.example.android.popularmovies.database.MovieObject;

import java.util.List;


public class MainViewModel extends AndroidViewModel {


    private LiveData<List<MovieObject>> favorites;

    public MainViewModel(Application application) {
        super(application);
        FavoriteDatabase database = FavoriteDatabase.getInstance(this.getApplication());
        favorites = database.favoriteDao().getFavorites();
    }

    public LiveData<List<MovieObject>> getFavorites() {
        return favorites;
    }
}