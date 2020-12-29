package com.example.recommendify4.UserInfo;

import com.example.recommendify4.SpotifyApi.RequestSender;
import com.example.recommendify4.SpotifyApi.ResponseProcessor;
import com.example.recommendify4.SpotifyItems.Artist;
import com.example.recommendify4.SpotifyItems.Song;
import com.example.recommendify4.SpotifyItems.User;
import java.util.ArrayList;


public class UserProfile implements Runnable{

    private Credentials credentials;
    private User user;
    private ArrayList<Song> topSongs;
    private ArrayList<Artist> topArtists;
    private ArrayList<Song> recentlyPlayedSongs;
    private ArrayList<Artist> recentlyPlayedArtists;


    public UserProfile(Credentials newCredentials){

        credentials = newCredentials;
        user = new User();
        recentlyPlayedSongs = new ArrayList<>();
        topSongs = new ArrayList<>();
        topArtists = new ArrayList<>();
        recentlyPlayedArtists = new ArrayList<>();

    }

    public Credentials getCredentials() {
        return credentials;
    }

    public User getUser() { return user; }

    public ArrayList<Artist> getRecentlyPlayedArtists() { return recentlyPlayedArtists; }

    public ArrayList<Song> getRecentlyPlayedSongs() {
        return recentlyPlayedSongs;
    }

    public ArrayList<Artist> getTopArtists() {
        return topArtists;
    }

    public ArrayList<Song> getTopSongs() {
        return topSongs;
    }

    public void setUser() {
        String responseString = RequestSender.getUserInfo(credentials);
        User newUser = ResponseProcessor.processUserInfoResponse(responseString);
        if(newUser != null) user = newUser;
    }


    public void setRecentlyPlayedSongs() {

        String response = RequestSender.getRecentlyPlayedSongs(credentials);
        ArrayList<Song> recentlyPlayedSongsList = ResponseProcessor.processRecentlyPlayedResponse(response);
        if(recentlyPlayedSongsList != null) recentlyPlayedSongs = recentlyPlayedSongsList;

    }

    public void setRecentlyPlayedArtists(){

        for(Song song : recentlyPlayedSongs) {
            ArrayList<Artist> artists = song.getArtists();
            for (Artist artist : artists) {
                if (!recentlyPlayedArtists.contains(artist)) recentlyPlayedArtists.add(artist);
                else artist.addOneToSongsInList();
            }
        }

    }

    public void setTopSongs() {

        String response = RequestSender.getTopSongs(credentials);
        ArrayList<Song> topSongsList = ResponseProcessor.processTopSongsResponse(response);
        if(topSongsList != null) topSongs = topSongsList;

    }

    public void setTopArtists() {

        String response = RequestSender.getTopArtists(credentials);
        ArrayList<Artist> topArtistsList = ResponseProcessor.processTopArtistsResponse(response, topSongs);
        if(topArtistsList != null) topArtists = topArtistsList;

    }

    @Override
    public void run() {
        setUser();
        setRecentlyPlayedSongs();
        setRecentlyPlayedArtists();
        setTopSongs();
        setTopArtists();
    }


}

