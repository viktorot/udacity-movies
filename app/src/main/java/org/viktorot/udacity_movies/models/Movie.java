package org.viktorot.udacity_movies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public int id;
    public String title;
    public String originalTitle;
    public String overview;
    public String backdropUrl;
    public String posterUrl;
    public float score;
    public String releaseDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.backdropUrl);
        dest.writeString(this.posterUrl);
        dest.writeFloat(this.score);
        dest.writeString(this.releaseDate);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.backdropUrl = in.readString();
        this.posterUrl = in.readString();
        this.score = in.readFloat();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
