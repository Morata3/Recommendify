package com.app.recommendify4.SpotifyItems;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Artist implements Parcelable{

    private String name;
    private String id;
    private ArrayList<String> genres;
    private int songsInList;
    private boolean recommended;

    public Artist(String artistName, ArrayList<String> artistGenres, String artistId){
        this.name = artistName;
        this.genres = artistGenres;
        this.id = artistId;
        this.songsInList = 1;
    }

    public Artist(String artistName, String artistId){
        this.name = artistName;
        this.genres = new ArrayList<>();
        this.id = artistId;
        this.songsInList = 1;
    }

    public Artist(String artistName){
        this.name = artistName;
        this.genres = new ArrayList<>();
    }

    public Artist(JSONObject artistJSON, int songsInList) throws JSONException {

        this.genres =  new ArrayList<>();
        this.name = artistJSON.getString("name");
        this.id = artistJSON.getString("id");
        JSONArray genres = artistJSON.getJSONArray("genres");
        for(int index = 0; index < genres.length(); index++) this.genres.add(genres.getString(index));
        this.songsInList = songsInList;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSongsInList() { return songsInList; }

    public void setSongsInList(int songsInList) { this.songsInList = songsInList; }

    public void setRecommended(boolean recommended) { this.recommended = recommended; }

    public boolean isRecommended() { return recommended; }

    public void addOneToSongsInList(){ this.songsInList++; }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.id.equals(((Artist) obj).id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", genres=" + genres +
                ", songsInList=" + songsInList +
                '}';
    }

    //PARCELABLE IMPLEMENTATION
    private Artist(Parcel in) {
        genres = new ArrayList<>();
        name = in.readString();
        id = in.readString();
        genres = in.readArrayList(null);
        songsInList = in.readInt();
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
        dest.writeInt(songsInList);
    }

}

