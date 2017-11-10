package example.aleperf.com.popmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;


public class MovieAsyncTask extends AsyncTask<Integer,Void,List<Movie>> {

    private Context context;

    public MovieAsyncTask(Context ctx){
        context = ctx;
    }
    @Override
    protected List<Movie> doInBackground(Integer... integers) {
        int page = integers[0];
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String querySearch = preferences.getString(context.getString(R.string.pref_search_key)
                , context.getString(R.string.pref_search_most_pop_value));
        URL queryURL = NetworkUtils.buildQueryURL(querySearch, page);
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
}
