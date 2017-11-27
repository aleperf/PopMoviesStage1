package example.aleperf.com.popmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.MovieServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> listOfMovies = new MutableLiveData<>();
    private int pageToLoad = 1;
    private boolean isLoading = false;
    private String lastPreference;
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    //  THE_MOVIE_DB_API_KEY = "YOUR API KEY HERE"
    private static final String MOVIE_DB_API_KEY = BuildConfig.MY_API_KEY;


    private MovieClient client = MovieServiceGenerator.createService(MovieClient.class);

    public MainActivityViewModel(Application application) {
        super(application);
        listOfMovies.setValue(new ArrayList<Movie>());
        loadMovies();

    }

    void loadMovies() {
        int MAX_PAGE = 3;
        if (pageToLoad <= MAX_PAGE && !isLoading) {
            isLoading = true;
            lastPreference = getMoviePreference();
            String preferenceSetting;
            if (lastPreference.equals(getApplication().getApplicationContext()
                    .getString(R.string.pref_search_most_pop_value))) {
                preferenceSetting = POPULAR;
            } else {
                preferenceSetting = TOP_RATED;
            }
            retrieveMovies(preferenceSetting, pageToLoad);
        }
    }

    MutableLiveData<List<Movie>> getMovies() {
        return listOfMovies;
    }


    boolean checkPreferencesChanged() {
        String actualPreference = getMoviePreference();
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
            public void onResponse(@NonNull Call<JSONUtils.MovieQuery> call, @NonNull Response<JSONUtils.MovieQuery> response) {
                JSONUtils.MovieQuery responseMovies = response.body();
                if (responseMovies != null) {
                    List<Movie> downloadedMovies = responseMovies.getResults();
                    List<Movie> currentMovies = listOfMovies.getValue();
                    if (downloadedMovies != null && currentMovies != null) {
                        currentMovies.addAll(downloadedMovies);
                        listOfMovies.setValue(currentMovies);
                        pageToLoad++;
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JSONUtils.MovieQuery> call, @NonNull Throwable t) {
                isLoading = false;
            }
        });


    }

    public interface MovieClient {

        @GET("{preference}")
        Call<JSONUtils.MovieQuery> getAllMovies(@Path("preference") String preferenceSetting,
                                                @Query("api_key") String apiKey, @Query("page") String page);


    }

    private String getMoviePreference() {
        Context context = this.getApplication().getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_search_key),
                context.getString(R.string.pref_search_most_pop_value));
    }
}
