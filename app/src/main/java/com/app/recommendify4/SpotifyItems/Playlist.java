package com.app.recommendify4.SpotifyItems;

import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;

import java.util.ArrayList;

public class Playlist {

    String name;
    String id;
    ArrayList<RecommendedSong> songs;

    public Playlist(String name, ArrayList<RecommendedSong> songs){
        this.name = name;
        this.songs = songs;
    }

    public ArrayList<RecommendedSong> getSongs() { return songs; }

    public String getName() { return name; }

    public String getId() { return id; }

    public void setName(String name) { this.name = name; }

    public void setSongs(ArrayList<RecommendedSong> songs) { this.songs = songs; }

    public void setId(String id) { this.id = id; }

}
