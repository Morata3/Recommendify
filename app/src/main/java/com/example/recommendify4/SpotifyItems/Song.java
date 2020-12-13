package com.example.recommendify4.SpotifyItems;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Song {

    private String name;
    private String album;
    private String id;
    private ArrayList<Artist> artists;
    private int timesInList;
    private boolean recommended;

    public Song(String songName, String albumName, ArrayList<Artist> artistsList, String songId, int timesInList){
        this.name = songName;
        this.album = albumName;
        this.artists = artistsList;
        this.id = songId;
        this.timesInList = timesInList;
    }

    public Song(JSONObject songInfo, int timesInList) throws JSONException {
        this.artists = new ArrayList<>();
        this.name = songInfo.getString("name");
        this.id = songInfo.getString("id");
        this.album = songInfo.getJSONObject("album").getString("name");
        JSONArray artistsJSON = songInfo.getJSONArray("artists");
        for(int artist = 0; artist < artistsJSON.length(); artist++){
            String artistName = artistsJSON.getJSONObject(artist).getString("name");
            String artistId = artistsJSON.getJSONObject(artist).getString("id");
            this.artists.add(new Artist(artistName, artistId));
        }
        this.timesInList = timesInList;

    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public String getAlbum() {
        return album;
    }

    public String getName() {
        return name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRecommended() { return recommended; }

    public void setRecommended(boolean recommended) { this.recommended = recommended; }

    public void addOneToTimesInList(){ this.timesInList++; }

    public int getTimesInList() { return timesInList; }

    public void setTimesInList(int timesInList) { this.timesInList = timesInList; }

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
                ", artists=" + artists +
                ", timesInList=" + timesInList +
                '}';
    }
}