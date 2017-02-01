package example.aleperf.com.popmovies;


import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {

    final private static byte NO_IMAGE = 0;
    final private static byte HAS_IMAGE = 1;
    final private String mOriginalTitle;
    final private String mTitle;
    final private String mPosterPath;
    final private String mPlotSynopsis;
    final private double mRating;
    final private byte mHasImage;
    final private String mReleaseDate;

    //Constructor
    public Movie(String originalTitle, String title, String path,
                 String plot, String date, double rating) {

        mOriginalTitle = originalTitle;
        mTitle = title;
        mPosterPath = path;
        if (path.equals("null")) {
            mHasImage = NO_IMAGE;
        } else {
            mHasImage = HAS_IMAGE;
        }
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
        mHasImage = in.readByte();
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
        return mPosterPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getRating() {
        return mRating;
    }

    public boolean hasImage() {

        return mHasImage == HAS_IMAGE;
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
        parcel.writeByte(mHasImage);
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
