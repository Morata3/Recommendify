package com.app.recommendify4.SpotifyItems.Song;

import com.app.recommendify4.SpotifyItems.Artist.Artist;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Song{

    private String name;
    private String album;
    private String id;



    public Song(){}

    public Song(String name, String album, String id){
        this.id = id;
        this.name = name;
        this.album = album;
    }

    public Song(String name, String id){
        this.id = id;
        this.name = name;
    }

    public Song(JSONObject songInfo/*, int timesInList*/) throws JSONException {
        this.name = songInfo.getString("name");
        this.id = songInfo.getString("id");
        this.album = songInfo.getJSONObject("album").getString("name");

    }

    public String getArtistNameList(ArrayList<RecommendedArtist> artists){
        String artistNames="";

        for (Artist a : artists){
            artistNames+=a.getName()+", ";
        }
        if (artistNames.length() > 0)
            artistNames = artistNames.substring(
                    0, artistNames.length() - 2);
        return artistNames;
    }

    public String getAlbum() {
        return album;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Song) obj).id);
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", album='" + album + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}