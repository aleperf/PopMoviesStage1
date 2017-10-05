package example.aleperf.com.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import example.aleperf.com.popmovies.utilities.JSONUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        MovieAdapter.MoviePosterClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String EXTRA_TAG = "example.aleperf.com.popmovies.selectedMovie";
    private final String CURRENT_PAGE = "current page";
    private final String LAST_PREFERENCE = "last_preference";
    private final String MOVIE_LIST = "movie list";

    //max number of pages of Movies loaded from TheMovieDb
    private final int MAX_NUM_PAGES = 3;
    private static final int MOVIE_LOADER_ID = 0;


    private ProgressBar mProgressBar;
    private ImageView mPopMovieLogo;
    private LinearLayout mEmptyView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private TextView mCurrentSettingsTextView;
    //next page to load in background
    private int mPageToLoad = 1;
    private List<Movie> mMovies;
    //true if there is a current loading in background
    private boolean mIsLoading = false;
    //the last preference seen when this activity is visible
    private String mLastPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCurrentSettingsTextView = (TextView) toolbar.findViewById(R.id.current_settings);
        displaySearchSettingsInToolbar();

        if (savedInstanceState != null) {
            mPageToLoad = savedInstanceState.getInt(CURRENT_PAGE, 1);
            mLastPreference = savedInstanceState.getString(LAST_PREFERENCE);
            if (!haveSearchPreferencesChanged()) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            } else {
                mMovies = null;
            }

        } else {
            mLastPreference = getCurrentSearchPreference();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        //inflate the numbers of columns from resources, based on the screen width in dp
        int numColumn = getResources().getInteger(R.integer.grid_layout_num_columns);
        GridLayoutManager gridManager = new GridLayoutManager(this, numColumn);
        mRecyclerView.setLayoutManager(gridManager);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        //set a listener on the recyclerView scroll to load more data if needed
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1) && !mIsLoading && mPageToLoad <= MAX_NUM_PAGES) {
                    //if mRecyclerView can't scroll down and the app isn't already loading something
                    //load another page
                    mIsLoading = true;
                    getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);


                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        mEmptyView = (LinearLayout) findViewById(R.id.empty_view_home_screen);
        mPopMovieLogo = (ImageView) findViewById(R.id.empty_view_image);
        mPopMovieLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, getString(R.string.toast_msg_empty_view), Toast.LENGTH_SHORT).show();
                resetInitialState();
                mIsLoading = true;
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
            }
        });


        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, MainActivity.this);


    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        if (mMovies == null || mIsLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        return new MovieLoader(this, mIsLoading, mPageToLoad);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (movies != null && movies.size() > 0) { //if there are new data, load them in mMovies

            if (mMovies == null) {
                mMovies = movies;
                mPageToLoad++;
                if (mIsLoading) {
                    mIsLoading = false;
                }

            } else if (mIsLoading) {
                mMovies.addAll(movies);
                mIsLoading = false;
                mPageToLoad++;

            }
        }

        if (mMovies != null) { //if mMovies is not null, load data in the adapter.
            mAdapter.setMovieData(mMovies);
            mAdapter.notifyDataSetChanged();
            hideEmptyMessage();
        } else {//no data to display, show the empty message
            showEmptyMessage();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        //do nothing;
    }


    /**
     * Return the value of the SharedPreferences for the movie search
     *
     * @return a string with the value of the current preference for movie search
     */
    private String getCurrentSearchPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(getString(R.string.pref_search_key), getString(R.string.pref_search_most_pop_value));

    }

    /**
     * Check if the last preference seen by this activity is the current preference chosen by the user.
     *
     * @return true if the preference is changed, false otherwise.
     */

    private boolean haveSearchPreferencesChanged() {
        return !mLastPreference.equals(getCurrentSearchPreference());
    }

    /**
     * Show the current setting for the movie search in the Toolbar of MainActivity,
     */
    private void displaySearchSettingsInToolbar() {
        String preference = getCurrentSearchPreference();
        String topRated = getString(R.string.pref_search_top_rated_value);
        if (preference.equals(topRated)) {
            mCurrentSettingsTextView.setText(getString(R.string.pref_search_top_rated_label));
        } else {
            mCurrentSettingsTextView.setText(getString(R.string.pref_search_most_pop_label));
        }
    }

    /**
     * Reset the fields necessary to start a search to the default values
     * and set the mLastPreference field to the value set in preferences
     */

    private void resetInitialState() {
        mLastPreference = getCurrentSearchPreference();
        mIsLoading = true;
        mPageToLoad = 1;
        mMovies = null;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mEmptyView.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        //if the preferences have been changed, reset the initial state e start a new search
        if (haveSearchPreferencesChanged()) {
            resetInitialState();
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);

        }
        displaySearchSettingsInToolbar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClickMoviePoster(int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Movie movie = mMovies.get(position);
        intent.putExtra(EXTRA_TAG, movie);
        startActivity(intent);

    }

    /**
     * Show the Empty Message - used when the RecyclerView is Empty
     */

    private void showEmptyMessage() {
        mEmptyView.setVisibility(View.VISIBLE);
        mPopMovieLogo.requestFocus();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Hide the Empty Message - called when the RecyclerView has something to show
     */

    private void hideEmptyMessage() {
        mEmptyView.setVisibility(View.GONE);
        mPopMovieLogo.clearFocus();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PAGE, mPageToLoad);
        mLastPreference = getCurrentSearchPreference();
        outState.putString(LAST_PREFERENCE, mLastPreference);
        outState.putParcelableArrayList(MOVIE_LIST, ((ArrayList<Movie>) mMovies));

    }


    public static class MovieLoader extends AsyncTaskLoader<List<Movie>> {

        List<Movie> mMovies;
        boolean mIsLoading;
        int mPageToLoad;

        public MovieLoader(Context context, boolean isLoading, int pageToLoad) {
            super(context);
            mIsLoading = isLoading;
            mPageToLoad = pageToLoad;
        }


        @Override
        protected void onStartLoading() {

            if (mMovies != null && !mIsLoading) {
                deliverResult(mMovies);
            } else {

                forceLoad();
            }
        }

        @Override
        public List<Movie> loadInBackground() {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String querySearch = preferences.getString(getContext().getString(R.string.pref_search_key), getContext().getString(R.string.pref_search_most_pop_value));
            URL queryURL = NetworkUtils.buildQueryURL(querySearch, mPageToLoad);
            List<Movie> movies = new ArrayList<>();
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpsUrl(queryURL);
                movies = JSONUtils.getMovieList(jsonResponse);
                return movies;

            } catch (Exception e) {
                e.printStackTrace();
                return movies;
            }

        }

        @Override
        public void deliverResult(List<Movie> data) {
            mMovies = data;
            super.deliverResult(data);
        }
    }


}




