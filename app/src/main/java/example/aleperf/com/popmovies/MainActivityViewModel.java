package example.aleperf.com.popmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> listOfMovies = new MutableLiveData<>();
    private static int MAX_PAGE = 3;
    private int pageToLoad = 1;
    private boolean isLoading = false;
    private String lastPreference;
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    //  THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
    private static final String MOVIE_DB_API_KEY = BuildConfig.MY_API_KEY;

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(MOVIE_DB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder
            .client(httpClient.build())
            .build();

    private MovieClient client = retrofit.create(MovieClient.class);

    public MainActivityViewModel(Application application) {
        super(application);
        Context context = application.getApplicationContext();
        listOfMovies.setValue(new ArrayList<Movie>());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        lastPreference = preferences.getString(context.getString(R.string.pref_search_key), context.getString(R.string.pref_search_most_pop_value));
        loadMovies();

    }

    public void loadMovies() {
        if (pageToLoad <= MAX_PAGE && !isLoading) {
            isLoading = true;
            Context context = this.getApplication().getApplicationContext();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            lastPreference = preferences.getString(context.getString(R.string.pref_search_key), context.getString(R.string.pref_search_most_pop_value));
            String preferenceSetting;
            if (lastPreference.equals(context.getString(R.string.pref_search_most_pop_value))) {
                preferenceSetting = POPULAR;
            } else {
                preferenceSetting = TOP_RATED;
            }
            retrieveMovies(preferenceSetting, pageToLoad);
        }
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return listOfMovies;
    }


    public boolean checkPreferencesChanged() {
        Context context = getApplication().getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String actualPreference = preferences.getString(context.getString(R.string.pref_search_key), context.getString(R.string.pref_search_most_pop_value));
        if (!actualPreference.equals(lastPreference)) {
            lastPreference = actualPreference;
            isLoading = false;
            listOfMovies.setValue(new ArrayList<Movie>());
            pageToLoad = 1;
            loadMovies();
            return true;
        }
        return false;
    }


    private void retrieveMovies(String preferenceSetting, int page) {

        Call<JSONUtils.MovieQuery> call = client.getAllMovies(preferenceSetting, MOVIE_DB_API_KEY, String.valueOf(page));

        call.enqueue(new Callback<JSONUtils.MovieQuery>() {
            @Override
            public void onResponse(Call<JSONUtils.MovieQuery> call, Response<JSONUtils.MovieQuery> response) {
                JSONUtils.MovieQuery responseMovies = response.body();
                List<Movie> downloadedMovies = responseMovies.getResults();
                List<Movie> currentMovies = listOfMovies.getValue();
                if (downloadedMovies != null && currentMovies != null) {
                    currentMovies.addAll(downloadedMovies);
                    listOfMovies.setValue(currentMovies);
                    pageToLoad++;
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<JSONUtils.MovieQuery> call, Throwable t) {
                isLoading = false;
            }
        });


    }

    public interface MovieClient {

        @GET("{preference}")
        Call<JSONUtils.MovieQuery> getAllMovies(@Path("preference") String preferenceSetting,
                                                @Query("api_key") String apiKey, @Query("page") String page);


    }
}
