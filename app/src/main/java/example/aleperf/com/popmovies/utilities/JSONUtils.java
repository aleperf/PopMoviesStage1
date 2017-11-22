package example.aleperf.com.popmovies.utilities;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import example.aleperf.com.popmovies.Movie;

/**
 * Provides static methods to extract data from JSON strings
 */

public class JSONUtils {

    public class MovieQuery {
        @SerializedName("status_code")
        Integer statusCode;
        @SerializedName("results")
        List<Movie> movieResults;

       public  List<Movie> getResults() {
            return movieResults;
        }

       public  Integer getStatusCode(){
            return statusCode;
        }


    }
}
