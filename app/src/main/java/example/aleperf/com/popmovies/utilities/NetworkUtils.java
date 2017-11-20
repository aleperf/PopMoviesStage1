package example.aleperf.com.popmovies.utilities;


import android.net.Uri;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import example.aleperf.com.popmovies.BuildConfig;


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

    //constants used to build the query to TheMovieDB
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    //TODO: insert the following line of code, with a valid API KEY between the quotes,
    // in the gradle.properties file:
    //  THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
    private static final String MOVIE_DB_API_KEY = BuildConfig.MY_API_KEY;
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_PAGE = "page";

    //constants used to decide which url to build
    private static final String QUERY_MOST_POPULAR = "query most popular";
    private static final String QUERY_TOP_RATED = "query top rated";

    //constants used to build a URL for requesting an image
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_FORMAT_W185 = "w185";
    private static final String IMAGE_FORMAT_W300 = "w300";
    private static final String IMAGE_FORMAT_W500 = "w500";


    /**
     * Build an URL based on the match of queryRequest to predefined constants
     *
     * @param queryRequest a String corresponding to a predefined constant in NetwokUtils
     * @return an URL of "theMovieDB" corresponding to the queryRequest.
     */

    @Nullable
    public static URL buildQueryURL(String queryRequest, int page) {
        Uri requestUri = buildQueryUri2(queryRequest, page);
        try {
            return new URL(requestUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * Build an Uri for "TheMovieDB" based on the the match of queryRequest to predefined constants,
     * if no matches is found return the uri for the most popular movies
     *
     * @param queryRequest a string  defining the type of uri request
     * @return an uri corresponding to the type of request
     */
    private static Uri buildQueryUri2(String queryRequest, int page) {
        String searchType;
        switch (queryRequest) {
            case QUERY_MOST_POPULAR:
                searchType = POPULAR;
                break;
            case QUERY_TOP_RATED:
                searchType = TOP_RATED;
                break;
            default:
                searchType = POPULAR;

        }

        return Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(searchType)
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                .build();

    }

    /**
     * Open a connection with the url specified as parameter and retrieve
     * a String representing a JSON query
     *
     * @param url the url to open the connection with
     * @return return a String representing a query in JSON
     * @throws IOException if the Url is malformed
     */

    public static String getResponseFromHttpsUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Build an return the uri to a movie poster in TheMovieDb
     *
     * @param imagePath the path of a specific image
     * @return the uri to the image
     */
    public static Uri buildImageUri(String imagePath) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_FORMAT_W185).appendPath(imagePath).build();
    }


    public static Uri buildBackdropImageUri(String imagePath) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_FORMAT_W500).appendPath(imagePath).build();
    }



}
