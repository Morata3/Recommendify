package com.app.recommendify4.SpotifyItems.Artist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserArtist extends Artist{
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

}
/*

    //PARCELABLE IMPLEMENTATION
    private Artist(Parcel in) {
        genres = new ArrayList<>();
        name = in.readString();
        id = in.readString();
        genres = in.readArrayList(null);
        //songsInList = in.readInt();
    }

    public static final Parcelable.Creator<Artist> CREATOR
            = new Parcelable.Creator<Artist>() {
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeList(genres);
        //dest.writeInt(songsInList);
    }
*/