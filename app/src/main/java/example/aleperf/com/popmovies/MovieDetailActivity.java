package example.aleperf.com.popmovies;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import example.aleperf.com.popmovies.utilities.MovieUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity {


    private static final String EXTRA_TAG = "example.aleperf.com.popmovies.selectedMovie";

    private String mOriginalTitle;
    private String mTitle;
    private String mDate;
    private double mRating;
    private String mPoster;
    private String mSynopsis;
    private boolean mMovieHasImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        MovieDetailViewModel model = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        Movie currentMovie;
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            currentMovie = intent.getParcelableExtra(EXTRA_TAG);
            extractDataFromMovie(currentMovie);
            model.setCurrentMovie(currentMovie);
        } else {
            currentMovie = model.getCurrentMovie();
            extractDataFromMovie(currentMovie);

        }

        TextView headerTextView = findViewById(R.id.header_title_detail);
        TextView dateTextView = findViewById(R.id.year_detail);
        TextView ratingTextView = findViewById(R.id.rating_detail);
        TextView originalTitleTextView = findViewById(R.id.original_title);
        TextView synopsisTextView = findViewById(R.id.synopsis_detail);
        ImageView posterImage = findViewById(R.id.movie_poster_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            try {
                String transitionName = currentMovie.getmMovieId();
                posterImage.setTransitionName(transitionName);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        headerTextView.setText(mTitle);
        originalTitleTextView.setText(mOriginalTitle);
        dateTextView.setText(MovieUtils.formatDate(mDate));
        ratingTextView.setText(MovieUtils.formatRating(mRating));
        synopsisTextView.setText(mSynopsis);
        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }
        };
        if (mMovieHasImage) {
            Log.d("uffa", "sono in has image, nome film: " + mTitle);
            Picasso.with(this).load(NetworkUtils.buildImageUri(mPoster)).fit().noFade().
                    into(posterImage, callback);

        } else {
            Log.d("uffa", "sono in else, nome film: " + mTitle);
            Picasso.with(this).load(R.drawable.no_preview_pop).fit().noFade().into(posterImage, callback);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings_detail:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * Extract data from the Movie passed from the caller activity
     * and load them into the UI
     *
     * @param movie the Movie passed from the caller activity to the MovieDetailActivity
     */

    private void extractDataFromMovie(Movie movie) {
        mOriginalTitle = movie.getOriginalTitle();
        mTitle = movie.getTitle();
        mDate = movie.getReleaseDate();
        mRating = movie.getRating();
        mPoster = movie.getPosterPath();
        mSynopsis = movie.getPlotSynopsis();
        mMovieHasImage = movie.hasImage();
    }

}
