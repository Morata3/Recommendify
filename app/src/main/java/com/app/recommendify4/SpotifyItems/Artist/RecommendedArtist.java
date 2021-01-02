package com.app.recommendify4.SpotifyItems.Artist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecommendedArtist extends Artist implements Parcelable {
    private int shown;

    public RecommendedArtist(String artistName, ArrayList<String> artistGenres, String artistId){
        super(artistName, artistGenres, artistId);
        shown = 0;
    }

    public RecommendedArtist(JSONObject artistJSON, int shown) throws JSONException {
        super(artistJSON);
        this.shown = shown;
    }

    public RecommendedArtist(String artistName, String artistId){
        super(artistName, artistId);
        shown = 0;
    }

    public RecommendedArtist(String artistName){
        super(artistName);
        shown = 0;
    }

    public boolean wasShown(){return shown != 0;}

    //PARCELABLE IMPLEMENTATION
    private RecommendedArtist(Parcel in) {
        this.setGenres(new ArrayList<>());
        this.setName(in.readString());
        this.setId(in.readString());
        this.setGenres(in.readArrayList(null));
        shown = in.readInt();
    }

    public static final Parcelable.Creator<RecommendedArtist> CREATOR
            = new Parcelable.Creator<RecommendedArtist>() {
        public RecommendedArtist createFromParcel(Parcel in) {
            return new RecommendedArtist(in);
        }

        public RecommendedArtist[] newArray(int size) {
            return new RecommendedArtist[size];
        }
    };
    public void setShown(int shown) { this.shown = shown; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getName());
        dest.writeString(this.getId());
        dest.writeList(this.getGenres());
        dest.writeInt(shown);
    }

    @NonNull
    @Override
    public String toString() {
        return "Artist{" +
                "name='" + this.getName() + '\'' +
                ", id='" + this.getId() + '\'' +
                ", genres=" + this.getGenres() +
                ", shown=" + shown +
                ", image="+this.getImage()+
                '}';
    }

}

