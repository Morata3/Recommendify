package com.example.recommendify4.UserInfo;

import java.util.ArrayList;

public class Song {

    private String name;
    private String album;
    private String id;
    private ArrayList<Artist> artists;
    private int timesInList;

    public Song(String songName, String albumName, ArrayList<Artist> artistsList, String songId){
        this.name = songName;
        this.album = albumName;
        this.artists = artistsList;
        this.id = songId;
        timesInList = 1;
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

