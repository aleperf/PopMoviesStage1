package example.aleperf.com.popmovies.utilities;



import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
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
     * @throws JSONException
     */

    public static List<Movie> getMovieList(String jsonString) {
        Gson gson = new Gson();
        MovieQuery query = gson.fromJson(jsonString, MovieQuery.class);
        List<Movie> listOfMovies = query.getResults();
        return listOfMovies;
    }


    public class MovieQuery {
        @SerializedName("results")
        List<Movie> movieResults;

        public List<Movie> getResults() {
            return movieResults;
        }


    }
}
