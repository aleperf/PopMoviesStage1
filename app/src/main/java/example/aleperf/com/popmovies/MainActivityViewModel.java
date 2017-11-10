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


public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> listOfMovies = new MutableLiveData<>();
    private int MAX_PAGE = 3;
    private int pageToLoad = 1;
    private boolean isLoading = false;
    private String lastPreference;

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
            List<Movie> movies = new ArrayList<>();
            MovieAsyncTask task = new MovieAsyncTask(this.getApplication());
            try {
                movies = task.execute(pageToLoad).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Movie> currentMovies = listOfMovies.getValue();
            if (currentMovies != null && movies.size() > 0) {
                currentMovies.addAll(movies);
                listOfMovies.setValue(currentMovies);
                pageToLoad++;
            } else {
                listOfMovies.setValue(new ArrayList<Movie>());
            }
            isLoading = false;
        }
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return listOfMovies;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void checkPreferencesChanged() {
        Context context = getApplication().getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String actualPreference = preferences.getString(context.getString(R.string.pref_search_key), context.getString(R.string.pref_search_most_pop_value));
        if (!actualPreference.equals(lastPreference)) {
            lastPreference = actualPreference;
            isLoading = false;
            listOfMovies.setValue(new ArrayList<Movie>());
            pageToLoad = 1;
            loadMovies();
        }
    }
}
