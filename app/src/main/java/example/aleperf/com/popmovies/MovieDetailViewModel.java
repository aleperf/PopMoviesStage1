package example.aleperf.com.popmovies;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;



public class MovieDetailViewModel extends ViewModel {
    @Nullable
    private Movie currentMovie;

    @Nullable
    public Movie getCurrentMovie(){
        return currentMovie;
    }

    public void setCurrentMovie(Movie movie){
        currentMovie = movie;
    }

}
