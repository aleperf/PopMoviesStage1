package example.aleperf.com.popmovies;


import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable {

    private final static String NO_IMAGE = "no image";


    @SuppressWarnings("unused")
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("id")
    final private String movieId;
    @SerializedName("original_title")
    final private String originalTitle;
    @SerializedName("title")
    final private String title;
    @SerializedName("overview")
    final private String plotSynopsis;
    @SerializedName("vote_average")
    final private double rating;
    @SerializedName("release_date")
    final private String releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("genre_ids")
    private int[] genreIds;


    //Constructor used by Parcelable to deserialize data
    private Movie(Parcel in) {
        movieId = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        plotSynopsis = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        genreIds = in.createIntArray();
    }

    String getMovieId() {
        return movieId;
    }

    String getOriginalTitle() {
        return originalTitle;
    }

    String getTitle() {
        return title;
    }

    String getPlotSynopsis() {
        return plotSynopsis;
    }

    String getPosterPath() {
        if (posterPath != null) {
            return posterPath.replace("/", "");
        }
        return NO_IMAGE;
    }

    public String getBackdropPath(){
        if(backdropPath != null){
            return backdropPath.replace("/", "");
        }
        return NO_IMAGE;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    double getRating() {
        return rating;
    }

    boolean hasImage() {

        return !(getPosterPath().equals(NO_IMAGE));
    }

    boolean hasBackdropPath(){
        return !(getBackdropPath().equals(NO_IMAGE));
    }

    int[] getGenredIds(){
        return genreIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //serialize parcelable data
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieId);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(plotSynopsis);
        parcel.writeDouble(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeIntArray(genreIds);

    }
}
