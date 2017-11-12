package example.aleperf.com.popmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MoviePosterClickListener {

    private static final String EXTRA_TAG = "example.aleperf.com.popmovies.selectedMovie";

    private ImageView mPopMovieLogo;
    private LinearLayout mEmptyView;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private TextView mCurrentSettingsTextView;

    private MainActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mEmptyView = findViewById(R.id.empty_view_home_screen);
        mPopMovieLogo = findViewById(R.id.empty_view_image);
        mCurrentSettingsTextView = toolbar.findViewById(R.id.current_settings);
        displaySearchSettingsInToolbar();
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        subscribe();
        List<Movie> mMovies = viewModel.getMovies().getValue();
        mRecyclerView = findViewById(R.id.recycler_view_main);
        //inflate the numbers of columns from resources, based on the screen width in dp
        int numColumn = getResources().getInteger(R.integer.grid_layout_num_columns);
        GridLayoutManager gridManager = new GridLayoutManager(this, numColumn);
        mRecyclerView.setLayoutManager(gridManager);
        mAdapter = new MovieAdapter(this);
        mAdapter.setMovieData(mMovies);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        //set a listener on the recyclerView scroll to load more data if needed
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1)) {
                    //if mRecyclerView can't scroll down and the app isn't already loading something
                    //load another page
                    viewModel.loadMovies();
                }
            }
        });
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getString(R.string.toast_msg_empty_view), Toast.LENGTH_SHORT).show();
                viewModel.loadMovies();
            }
        });
        if (mMovies == null || mMovies.size() == 0) {
            showEmptyMessage();
        } else {
            hideEmptyMessage();
        }
    }

    private void subscribe() {
        final Observer<List<Movie>> movieObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> listOfMovies) {
                if (listOfMovies != null) {
                    mAdapter.setMovieData(listOfMovies);
                    if (listOfMovies.size() == 0) {
                        showEmptyMessage();
                    } else {
                        hideEmptyMessage();
                    }
                } else {
                    showEmptyMessage();
                }


            }
        };

        viewModel.getMovies().observe(this, movieObserver);
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


    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkPreferencesChanged();
        displaySearchSettingsInToolbar();
        List<Movie> movies = viewModel.getMovies().getValue();
        if (movies == null || movies.size() == 0) {
            showEmptyMessage();
        } else {
            hideEmptyMessage();
        }
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
    public void onClickMoviePoster(int position, ImageView sharedImageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Movie movie = viewModel.getMovies().getValue().get(position);
        intent.putExtra(EXTRA_TAG, movie);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String transitionName = ViewCompat.getTransitionName(sharedImageView);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    sharedImageView,
                    transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    /**
     * Show the Empty Message - used when the RecyclerView is Empty
     */
    private void showEmptyMessage() {
        mEmptyView.setVisibility(View.VISIBLE);
        mPopMovieLogo.requestFocus();
    }

    /**
     * Hide the Empty Message - called when the RecyclerView has something to show
     */

    private void hideEmptyMessage() {
        mEmptyView.setVisibility(View.GONE);
        mPopMovieLogo.clearFocus();
    }


}




