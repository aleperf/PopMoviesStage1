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
 *
 *   This class requires a valid API KEY to query TheMovieDb, the API KEY is build from
 *    the constant THE_MOVIE_DB_API_KEY in the gradle.properties file.
 *    In order to make this class work, include this line of code in your
 *    gradle.properties file in the gradle folder,
 *    putting a valid API KEY between quotes:
 *    THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
 *
 *    your can request a valid API KEY to query TheMovieDb here:
 *    https://www.themoviedb.org/documentation/api
 *
 *    If your are sharing this code somewhere, please remember to hide your API KEY and put
 *    the build.gradle file in the .gitignore file, or take whatever measures necessary to prevent
 *    your API KEY to be shared publicly, as required by the TheMovieDb terms of use.
 *
 *
 */

public class NetworkUtils {

    //constants used to build the query to TheMovieDB

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    //TODO: insert the following line of code, with a valid API KEY between the quotes,
    // in the gradle.properties file:
    //  THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
    private static final String MOVIE_DB_API_KEY = BuildConfig.MY_API_KEY;
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAM_PAGE = "page";
    //add a filter to limit high rated, but too little known movies, based on number of ratings
    // VOTE_COUNT_GTE should be greater then or equal to 300
    private static final String PARAM_VOTE_COUNT_GTE = "vote_count.gte";
    private static final String  VOTE_COUNT_GTE = "300";

    private static final String SORT_POPULARITY_DESC = "popularity.desc";
    private static final String SORT_TOP_RATED_DESC = "vote_average.desc";

    //constants used to decide which url to build
    private static final String QUERY_MOST_POPULAR = "query most popular";
    private static final String QUERY_TOP_RATED = "query top rated";

    //constants used to build a URL for requesting an image
    private static final String IMAGE_BASE_URL ="http://image.tmdb.org/t/p/";
    private static final String IMAGE_FORMAT_W185="w185";


    /**
     * Build an URL based on the match of queryRequest to predefined constants
     *
     *@param queryRequest a String corresponding to a predefined constant in NetwokUtils
     * @return an URL of "theMovieDB" corresponding to the queryRequest.
     */

    @Nullable
    public static URL buildQueryURL(String queryRequest, int page){
        Uri requestUri = buildQueryUri(queryRequest, page);
        try{
            URL url = new URL(requestUri.toString());
            return url;
        }catch(MalformedURLException e){
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
    private static Uri buildQueryUri(String queryRequest, int page){

        Uri.Builder builder = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, MOVIE_DB_API_KEY)
                .appendQueryParameter(PARAM_VOTE_COUNT_GTE, VOTE_COUNT_GTE);

        switch(queryRequest){
            case QUERY_MOST_POPULAR:
                   builder= builder.appendQueryParameter(PARAM_SORT_BY, SORT_POPULARITY_DESC);
                   break;

            case QUERY_TOP_RATED:
                   builder =  builder.appendQueryParameter(PARAM_SORT_BY, SORT_TOP_RATED_DESC);
                   break;

            default:
                builder = builder.appendQueryParameter(PARAM_SORT_BY, SORT_POPULARITY_DESC);


        }
        return builder.appendQueryParameter(PARAM_PAGE, String.valueOf(page)).build();
    }

    /**
     * Open a connection with the url specified as parameter and retrieve
     * a String representing a JSON query
     * @param url the url to open the connection with
     * @return return a String representing a query in JSON
     * @throws IOException
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
     * @param imagePath the path of a specific image
     * @return the uri to the image
     */
    public static Uri buildImageUri(String imagePath){
        Uri uri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_FORMAT_W185).appendPath(imagePath).build();

        return uri;
    }




}
