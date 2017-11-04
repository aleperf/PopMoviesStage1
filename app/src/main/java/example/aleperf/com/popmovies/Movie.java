package example.aleperf.com.popmovies;


import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable {

    private final static String NO_POSTER = "no poster";

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
    final private String mMovieId;
    @SerializedName("original_title")
    final private String mOriginalTitle;
    @SerializedName("title")
    final private String mTitle;
    @SerializedName("overview")
    final private String mPlotSynopsis;
    @SerializedName("vote_average")
    final private double mRating;
    @SerializedName("release_date")
    final private String mReleaseDate;
    @SerializedName("poster_path")
    private String mPosterPath;

    //Constructor

    /**
     * public Movie(String movieId, String originalTitle, String title, String path,
     * String plot, String date, double rating) {
     * mMovieId = movieId;
     * mOriginalTitle = originalTitle;
     * mTitle = title;
     * mPosterPath = path;
     * mPlotSynopsis = plot;
     * mReleaseDate = date;
     * this.mRating = rating;
     * }
     **/

    //Constructor used by Parcelable to deserialize data
    private Movie(Parcel in) {
        mMovieId = in.readString();
        mOriginalTitle = in.readString();
        mTitle = in.readString();
        mPosterPath = in.readString();
        mPlotSynopsis = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
    }

    String getmMovieId() {
        return mMovieId;
    }

    String getOriginalTitle() {
        return mOriginalTitle;
    }

    String getTitle() {
        return mTitle;
    }

    String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    String getPosterPath() {
        if (mPosterPath != null) {
            return mPosterPath.replace("/", "");
        }
        return NO_POSTER;
    }

    String getReleaseDate() {
        return mReleaseDate;
    }

    double getRating() {
        return mRating;
    }

    boolean hasImage() {

        return !(getPosterPath().equals(NO_POSTER));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //serialize parcelable data
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMovieId);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mPlotSynopsis);
        parcel.writeDouble(mRating);
        parcel.writeString(mReleaseDate);

    }
}
