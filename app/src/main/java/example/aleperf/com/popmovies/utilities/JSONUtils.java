package example.aleperf.com.popmovies.utilities;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import example.aleperf.com.popmovies.Movie;

/**
 * Provides static methods to extract data from JSON strings
 */

public class JSONUtils {


    /**
     * Build a list of movies from a JSON string obtained from a query to TheMovieDb
     * @param jsonString a JSON string returned by a query to TheMovieDb
     * @return a list of Movies

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

        return query.getResults();
    }


    public class MovieQuery {
        @SerializedName("status_code")
        Integer statusCode;
        @SerializedName("results")
        List<Movie> movieResults;

        List<Movie> getResults() {
            return movieResults;
        }

        Integer getStatusCode(){
            return statusCode;
        }


    }
}
