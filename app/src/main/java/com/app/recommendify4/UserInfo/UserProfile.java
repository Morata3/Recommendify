package com.app.recommendify4.UserInfo;

import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyApi.ResponseProcessor;
import com.app.recommendify4.SpotifyItems.Artist.Artist;
import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.Song.Song;
import com.app.recommendify4.SpotifyItems.Song.UserSong;
import com.app.recommendify4.SpotifyItems.User;

import java.util.ArrayList;


public class UserProfile implements Runnable{

    private Credentials credentials;
    private User user;
    private ArrayList<UserSong> topSongs;
    private ArrayList<UserArtist> topArtists;
    private ArrayList<UserSong> recentlyPlayedSongs;
    private ArrayList<UserArtist> recentlyPlayedArtists;


    public UserProfile(Credentials newCredentials){

        credentials = newCredentials;
        user = new User();
        recentlyPlayedSongs = new ArrayList<>();
        topSongs = new ArrayList<>();
        topArtists = new ArrayList<>();
        recentlyPlayedArtists = new ArrayList<>();

    }


    public User getUser() { return user; }

    public Credentials getCredentials(){return credentials; }

    public ArrayList<UserArtist> getRecentlyPlayedArtists() { return recentlyPlayedArtists; }

    public ArrayList<UserSong> getRecentlyPlayedSongs() {
        return recentlyPlayedSongs;
    }

    public ArrayList<UserArtist> getTopArtists() {
        return topArtists;
    }

    public ArrayList<UserSong> getTopSongs() {
        return topSongs;
    }

    public void setUser() {
        String responseString = RequestSender.getUserInfo(credentials);
        User newUser = ResponseProcessor.processUserInfoResponse(responseString);
        if(newUser != null) user = newUser;
    }


    public void setRecentlyPlayedSongs() {

        String response = RequestSender.getRecentlyPlayedSongs(credentials);
        ArrayList<UserSong> recentlyPlayedSongsList = ResponseProcessor.processRecentlyPlayedResponse(response);
        if(recentlyPlayedSongsList != null) recentlyPlayedSongs = recentlyPlayedSongsList;

    }

    public void setRecentlyPlayedArtists(){

        for(UserSong song : recentlyPlayedSongs) {
            ArrayList<UserArtist> artists = song.getArtists();
            for (UserArtist artist : artists) {
                if (!recentlyPlayedArtists.contains(artist)) recentlyPlayedArtists.add(artist);
                else artist.incrementSongsInList();
            }
        }

    }

    public void setTopSongs() {

        String response = RequestSender.getTopSongs(credentials);
        ArrayList<UserSong> topSongsList = ResponseProcessor.processTopSongsResponse(response);
        if(topSongsList != null) topSongs = topSongsList;

    }

    public void setTopArtists() {

        String response = RequestSender.getTopArtists(credentials);
        ArrayList<UserArtist> topArtistsList = ResponseProcessor.processTopArtistsResponse(response, topSongs);
        if(topArtistsList != null) topArtists = topArtistsList;

    }

    public synchronized int getLastSongUsed(){
        for(UserSong song : topSongs) if(!song.isUsed()) return topSongs.indexOf(song);

        return 0;
    }

    public synchronized int getLastArtistUsed(){
        for(UserArtist artist : topArtists) if(!artist.isUsed()) return topArtists.indexOf(artist);

        return 0;
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

