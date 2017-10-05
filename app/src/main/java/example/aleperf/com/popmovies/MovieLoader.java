package example.aleperf.com.popmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.preference.PreferenceManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

/**
 * Created by Tundra on 05/10/2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    List<Movie> mMovies;
    boolean mIsLoading;
    int mPageToLoad;

    MovieLoader(Context context, boolean isLoading, int pageToLoad) {
        super(context);
        mIsLoading = isLoading;
        mPageToLoad = pageToLoad;
    }


    @Override
    protected void onStartLoading() {

        if (mMovies != null && !mIsLoading) {
            deliverResult(mMovies);
        } else {

            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String querySearch = preferences.getString(getContext().getString(R.string.pref_search_key), getContext().getString(R.string.pref_search_most_pop_value));
        URL queryURL = NetworkUtils.buildQueryURL(querySearch, mPageToLoad);
        List<Movie> movies = new ArrayList<>();
        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpsUrl(queryURL);
            movies = JSONUtils.getMovieList(jsonResponse);
            return movies;

        } catch (Exception e) {
            e.printStackTrace();
            return movies;
        }

    }

    @Override
    public void deliverResult(List<Movie> data) {
        mMovies = data;
        super.deliverResult(data);
    }
}
