package example.aleperf.com.popmovies.utilities;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieServiceGenerator {
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(MOVIE_DB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    private static Retrofit retrofit = builder.client(httpClient.build()).build();


    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
