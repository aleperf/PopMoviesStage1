package example.aleperf.com.popmovies.utilities;


import android.net.Uri;

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
