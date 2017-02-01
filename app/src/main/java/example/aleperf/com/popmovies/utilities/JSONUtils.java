package example.aleperf.com.popmovies.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.List;
import java.util.ArrayList;
import example.aleperf.com.popmovies.Movie;

/**
 * Provides static methods to extract data from JSON strings
 */

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();
    private static final String ARRAY_OF_MOVIES = "results";
    private static final String MOVIE_ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String SYNOPSIS = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String TITLE = "title";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String RATING = "vote_average";

    private static final String STATUS_CODE = "status_code";
    private static final int INVALID_API_KEY = 7;
    private static final int NOT_FOUND = 34;

    /**
     * Build a list of movies from a JSON string obtained from a query to TheMovieDb
     * @param jsonString a JSON string returned by a query to TheMovieDb
     * @return a list of Movies
     * @throws JSONException
     */

    public static List<Movie> getMovieList(String jsonString) throws JSONException {

        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject movieJson = new JSONObject(jsonString);

        // Is there an error?
        //TheMovieDb send a status code only in case of errors

        if (movieJson.has(STATUS_CODE)) {
            int errorCode = movieJson.getInt(STATUS_CODE);
            switch (errorCode) {
                case INVALID_API_KEY:
                    Log.d(TAG, "Invalid API KEY");
                    return null;
                case NOT_FOUND:
                    Log.d(TAG, "bad request");
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray results = movieJson.getJSONArray(ARRAY_OF_MOVIES);

        for(int i = 0; i< results.length(); i++){
            JSONObject movieItem = results.getJSONObject(i);
            String originalTitle = movieItem.getString(ORIGINAL_TITLE);
            String title = movieItem.getString(TITLE);
            String posterPath = movieItem.getString(POSTER_PATH).replace("/","");
            String plotSynopsis = movieItem.getString(SYNOPSIS);
            String releaseDate = movieItem.getString(RELEASE_DATE);
            double rating = movieItem.getDouble(RATING);
            Movie movie = new Movie(originalTitle, title, posterPath, plotSynopsis, releaseDate, rating);
            movieList.add(movie);


        }

        return movieList;
    }


}
