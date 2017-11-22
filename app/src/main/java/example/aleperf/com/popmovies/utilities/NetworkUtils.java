package example.aleperf.com.popmovies.utilities;


import android.net.Uri;



/**
 * Provides static CONSTANTS and  methods to build URLs for querying the TheMovieDb
 * <p>
 * This class requires a valid API KEY to query TheMovieDb, the API KEY is build from
 * the constant THE_MOVIE_DB_API_KEY in the gradle.properties file.
 * In order to make this class work, include this line of code in your
 * gradle.properties file in the gradle folder,
 * putting a valid API KEY between quotes:
 * THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
 * <p>
 * your can request a valid API KEY to query TheMovieDb here:
 * https://www.themoviedb.org/documentation/api
 * <p>
 * If your are sharing this code somewhere, please remember to hide your API KEY and put
 * the build.gradle file in the .gitignore file, or take whatever measures necessary to prevent
 * your API KEY to be shared publicly, as required by the TheMovieDb terms of use.
 */

public class NetworkUtils {



    //constants used to build a URL for requesting an image
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_FORMAT_W185 = "w185";
    private static final String IMAGE_FORMAT_W300 = "w300";
    private static final String IMAGE_FORMAT_W500 = "w500";





    public static Uri buildImageUri(String imagePath) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_FORMAT_W185).appendPath(imagePath).build();
    }


    public static Uri buildBackdropImageUri(String imagePath) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_FORMAT_W500).appendPath(imagePath).build();
    }
}
