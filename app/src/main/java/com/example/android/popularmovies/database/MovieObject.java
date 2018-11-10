package com.example.android.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MovieObject {
    private String mTitle;
    private String mPosterPath;
    private String mPlotSynopsis;
    private String mVoteAverage;
    private String mReleaseDate;
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;


    public MovieObject(String title, String posterPath, String plotSynopsis, String voteAverage, String releaseDate, int id){
        mTitle = title;
        mPosterPath = posterPath;
        mPlotSynopsis = plotSynopsis;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
        mId = id;
    }
    @Ignore
    public MovieObject(List<String> details){
        if (details.size() == 6){
            mPosterPath = details.get(0);
            mTitle = details.get(1);
            mVoteAverage = details.get(2);
            mReleaseDate = details.get(3);
            mPlotSynopsis = details.get(4);
            mId = Integer.parseInt(details.get(5));
        }
    }
    public ArrayList<String> printDetails(){
        ArrayList<String> details = new ArrayList<>();
        details.add(mPosterPath);
        details.add(mTitle);
        details.add(mVoteAverage);
        details.add(mReleaseDate);
        details.add(mPlotSynopsis);
        details.add(mId + "");
        Log.e("Stuff", "details" + details.size());
        return details;

    }
    public String printMovieObject() {
        return mTitle + mPosterPath + mPlotSynopsis + mVoteAverage + mReleaseDate;
    }

    public String getMTitle() {
        return mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMPosterPath() {
        return mPosterPath;
    }

    public void setMPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getMPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setMPlotSynopsis(String mPlotSynopsis) {
        this.mPlotSynopsis = mPlotSynopsis;
    }

    public String getMVoteAverage() {
        return mVoteAverage;
    }

    public void setMVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getMReleaseDate() {
        return mReleaseDate;
    }

    public void setMReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public int getMId() {
        return mId;
    }

    public void setMId(int mId) {
        this.mId = mId;
    }
}
