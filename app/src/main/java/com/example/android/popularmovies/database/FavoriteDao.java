package com.example.android.popularmovies.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("Select * FROM movieobject")
    LiveData<List<MovieObject>> getFavorites();

    @Query("Select * from movieobject where id = :id")
    MovieObject returnFavoriteById(int id);

    @Insert
    void insertFavorite(MovieObject movie);

    @Delete
    void deleteFavorite(MovieObject movie);

}
