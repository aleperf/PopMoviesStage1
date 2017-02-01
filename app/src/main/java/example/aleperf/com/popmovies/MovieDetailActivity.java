package example.aleperf.com.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import example.aleperf.com.popmovies.utilities.MovieUtils;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity {


    public static final String MOVIE_DETAIL = "movie detail";
    
    private final String EXTRA_TAG = "example.aleperf.com.popmovies.selectedMovie";

    private String mOriginalTitle;
    private String mTitle;
    private String mDate;
    private double mRating;
    private String mPoster;
    private String mSynopsis;
    private boolean mMovieHasImage;
    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mMovie = intent.getParcelableExtra(EXTRA_TAG);
            extractDataFromMovie(mMovie);
        } else {
           mMovie = savedInstanceState.getParcelable(MOVIE_DETAIL);
            extractDataFromMovie(mMovie);
           
        }
        
        TextView headerTextView = (TextView)findViewById(R.id.header_title_detail);
        TextView dateTextView = (TextView) findViewById(R.id.year_detail);
        TextView ratingTextView = (TextView)findViewById(R.id.rating_detail);
        TextView originalTitleTextView = (TextView)findViewById(R.id.original_title);
        TextView synopsisTextView = (TextView)findViewById(R.id.synopsis_detail);
        ImageView posterImage = (ImageView)findViewById(R.id.movie_poster_detail);
        
        headerTextView.setText(mTitle);
        originalTitleTextView.setText(mOriginalTitle);
        dateTextView.setText(MovieUtils.formatDate(mDate));
        ratingTextView.setText(MovieUtils.formatRating(mRating));
        synopsisTextView.setText(mSynopsis);
        if(mMovieHasImage){
            Picasso.with(this).load(NetworkUtils.buildImageUri(mPoster)).fit().into(posterImage);
        } else {
            Picasso.with(this).load(R.drawable.no_preview_pop).fit().into(posterImage);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        switch(id){
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
     * @param movie the Movie passed from the caller activity to the MovieDetailActivity
     */
    
    private void extractDataFromMovie(Movie movie){
        mOriginalTitle = movie.getOriginalTitle();
        mTitle = movie.getTitle();
        mDate = movie.getReleaseDate();
        mRating = movie.getRating();
        mPoster = movie.getPosterPath();
        mSynopsis = movie.getPlotSynopsis();
        mMovieHasImage = movie.hasImage();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       outState.putParcelable(MOVIE_DETAIL,mMovie);
    }
}
