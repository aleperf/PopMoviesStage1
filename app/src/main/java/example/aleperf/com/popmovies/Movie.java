package example.aleperf.com.popmovies;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable {


    @SerializedName("original_title")
    final private String mOriginalTitle;
    @SerializedName("title")
    final private String mTitle;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("overview")
    final private String mPlotSynopsis;
    @SerializedName("vote_average")
    final private double mRating;
    @SerializedName("release_date")
    final private String mReleaseDate;

    //Constructor
    public Movie(String originalTitle, String title, String path,
                 String plot, String date, double rating) {

        mOriginalTitle = originalTitle;
        mTitle = title;
        mPosterPath = path;
        mPlotSynopsis = plot;
        mReleaseDate = date;
        this.mRating = rating;
    }

    //Constructor used by Parcelable to deserialize data
    protected Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mTitle = in.readString();
        mPosterPath = in.readString();
        mPlotSynopsis = in.readString();
        mRating = in.readDouble();

        mReleaseDate = in.readString();
    }


    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getPosterPath() {
        return mPosterPath.replace("/", "");
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getRating() {
        return mRating;
    }

    public boolean hasImage() {

      if(getPosterPath() == null){
          return false;
      }
      return true;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //serialize parcelable data
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mPlotSynopsis);
        parcel.writeDouble(mRating);
        parcel.writeString(mReleaseDate);

    }

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
}
