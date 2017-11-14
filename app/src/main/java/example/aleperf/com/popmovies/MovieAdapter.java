package example.aleperf.com.popmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import example.aleperf.com.popmovies.utilities.NetworkUtils;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Context context;
    private List<Movie> movies;


    /**
     * Interface used to manage the click on a Movie Poster
     */
    public interface MoviePosterClickListener {

        void onClickMoviePoster(int position, ImageView sharedImageView);
    }


    MovieAdapter(Context context) {
        this.context = context;

    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_list_main, parent, false);
        return new MovieHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bindMovie(movie);
        ViewCompat.setTransitionName(holder.poster, movie.getMovieId());

    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.size();
    }

    void setMovieData(List<Movie> data) {
        movies = data;
        notifyDataSetChanged();
    }


    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView movieTitle;

        MovieHolder(View view) {
            super(view);
            poster = view.findViewById(R.id.movie_poster);
            movieTitle = view.findViewById(R.id.title_no_image_preview);
            poster.setOnClickListener(this);
        }

        void bindMovie(Movie movie) {

            if (movie.hasImage()) {
                String path = movie.getPosterPath();
                Uri imagePath = NetworkUtils.buildImageUri(path);
                Picasso.with(context).load(imagePath).fit().error(R.drawable.no_preview_pop).into(poster);
                movieTitle.setVisibility(View.INVISIBLE);
            } else {// if the movie hasn't a poster load the default image and show the movie title
                Picasso.with(context).load(R.drawable.no_preview_pop).into(poster);
                movieTitle.setText(movie.getOriginalTitle());
                movieTitle.setVisibility(View.VISIBLE);
            }

        }


        @Override
        public void onClick(View view) {
            if (context instanceof MoviePosterClickListener) {
                MoviePosterClickListener listener = (MoviePosterClickListener) context;
                listener.onClickMoviePoster(getAdapterPosition(), poster);
            }
        }


    }


}
