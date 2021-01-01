package com.app.recommendify4.SpotifyItems.Artist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.recommendify4.SpotifyItems.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserArtist extends Artist implements Parcelable{
    private int songsInList;

    public UserArtist(String artistName, ArrayList<String> artistGenres, String artistId){
        super(artistName, artistGenres, artistId);
        this.songsInList = 1;
    }

    public UserArtist(String artistName, String artistId){
        super(artistName, artistId);
        this.songsInList = 1;
    }

    public UserArtist(JSONObject artistJSON, int songsInList) throws JSONException {
        super(artistJSON);
        this.songsInList = songsInList;
    }


    public int getSongsInList() { return songsInList; }

    public void setSongsInList(int songsInList) { this.songsInList = songsInList; }

    public void incrementSongsInList(){ this.songsInList++; }

    @NonNull
    @Override
    public String toString() {
        return "Artist{" +
                "name='" + this.getName() + '\'' +
                ", id='" + this.getId() + '\'' +
                ", genres=" + this.getGenres() +
                ", songsInList=" + songsInList +
                '}';
    }

    //PARCELABLE IMPLEMENTATION
    private UserArtist(Parcel in) {
        this.setGenres(new ArrayList<>());
        this.setName(in.readString());
        this.setId(in.readString());
        this.setGenres(in.readArrayList(null));
        songsInList = in.readInt();
    }

    public static final Parcelable.Creator<UserArtist> CREATOR
            = new Parcelable.Creator<UserArtist>() {
        public UserArtist createFromParcel(Parcel in) {
            return new UserArtist(in);
        }

        public UserArtist[] newArray(int size) {
            return new UserArtist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getName());
        dest.writeString(this.getId());
        dest.writeList(this.getGenres());
        dest.writeInt(songsInList);
    }


}