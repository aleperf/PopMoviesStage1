package example.aleperf.com.popmovies.utilities;



import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import java.util.List;
import example.aleperf.com.popmovies.Movie;

/**
 * Provides static methods to extract data from JSON strings
 */

public class JSONUtils {

    private static final int INVALID_API_KEY = 7;
    private static final int NOT_FOUND = 34;


    /**
     * Build a list of movies from a JSON string obtained from a query to TheMovieDb
     * @param jsonString a JSON string returned by a query to TheMovieDb
     * @return a list of Movies
     * @throws JSONException
     */

    public static List<Movie> getMovieList(String jsonString) {

        Gson gson = new Gson();
        MovieQuery query = gson.fromJson(jsonString, MovieQuery.class);
        Integer statusCode = query.getStatusCode();
        //TheMovieDB sends a status code only in case of error
        //if there is a status code, the query is invalid
        if(statusCode != null){
            return null;
        }
        List<Movie> listOfMovies = query.getResults();
        return listOfMovies;
    }


    public class MovieQuery {
        @SerializedName("status_code")
        Integer statusCode;
        @SerializedName("results")
        List<Movie> movieResults;

        public List<Movie> getResults() {
            return movieResults;
        }

        public Integer getStatusCode(){
            return statusCode;
        }


    }
}
