package com.app.recommendify4.UserInfo;

import com.app.recommendify4.SpotifyItems.Song;

import java.util.ArrayList;

public class UserRecommendations {

    private Credentials credentials;
    private ArrayList<Song> historicalSongs;
    private Song song;
    private String trackId;

    public UserRecommendations(Credentials newCredentials, Song song){
        this.credentials = newCredentials;
        this.historicalSongs = new ArrayList<>();
        this.trackId = song.getId();
        this.song = song;
    }

    public ArrayList<Song> getRecommendedSongs(){return historicalSongs; }

}
