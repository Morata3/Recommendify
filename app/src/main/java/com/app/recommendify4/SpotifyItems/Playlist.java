package com.app.recommendify4.SpotifyItems;

import java.util.ArrayList;

public class Playlist {

    String name;
    String id;
    ArrayList<Song> songs;

    public Playlist(String name, ArrayList<Song> songs){
        this.name = name;
        this.songs = songs;
    }

    public ArrayList<Song> getSongs() { return songs; }

    public String getName() { return name; }

    public String getId() { return id; }

    public void setName(String name) { this.name = name; }

    public void setSongs(ArrayList<Song> songs) { this.songs = songs; }

    public void setId(String id) { this.id = id; }

}
