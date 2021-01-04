package com.app.recommendify4.SpotifyItems.Artist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.recommendify4.SpotifyApi.ResponseProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Artist/* implements Parcelable*/{

    private String name;
    private String id;
    private ArrayList<String> genres;
    private String image;


    public Artist(){

    }
    public Artist(String artistName, ArrayList<String> artistGenres, String artistId){
        this.name = artistName;
        this.genres = artistGenres;
        this.id = artistId;

    }

    public Artist(String artistName, String artistId){
        this.name = artistName;
        this.genres = new ArrayList<>();
        this.id = artistId;

    }

    public Artist(String artistName){
        this.name = artistName;
        this.genres = new ArrayList<>();
    }

    public Artist(JSONObject artistJSON/*, int songsInList*/) throws JSONException {

        this.genres =  new ArrayList<>();
        this.name = artistJSON.getString("name");
        this.id = artistJSON.getString("id");
        JSONArray genres = artistJSON.getJSONArray("genres");
        this.image = ResponseProcessor.processArtistImage(artistJSON);
        for(int index = 0; index < genres.length(); index++) this.genres.add(genres.getString(index));

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

    public String getImage(){
        return image;
    }

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
                '}';
    }

}
