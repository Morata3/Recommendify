package com.app.recommendify4.UserInfo;

import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.Song;

import java.util.ArrayList;

public class Recommendations {

    private ArrayList<RecommendedSong> songsRecommendations;
    private ArrayList<RecommendedArtist> artistRecommendations;

    private ArrayList<RecommendedSong> songsShown;
    private ArrayList<RecommendedArtist> artistsShown;

    public Recommendations(){
        this.songsRecommendations = new ArrayList<>();
        this.artistRecommendations = new ArrayList<>();
        this.songsShown = new ArrayList<>();
        this.artistsShown = new ArrayList<>();
    }

    public ArrayList<RecommendedArtist> getArtistRecommendations() { return artistRecommendations; }

    public ArrayList<RecommendedArtist> getArtistsShown() { return artistsShown; }

    public ArrayList<RecommendedSong> getSongsRecommendations() { return songsRecommendations; }

    public ArrayList<RecommendedSong> getSongsShown() { return songsShown; }

    public void addSongRecommendations(ArrayList<RecommendedSong> newRecommendations){
        this.songsRecommendations.addAll(newRecommendations);
    }

    public void addArtistRecommendations(ArrayList<RecommendedArtist> newRecommendations){
        this.artistRecommendations.addAll(newRecommendations);
    }

    public void moveToHistory(RecommendedSong song){
        this.songsRecommendations.remove(song);
        this.songsShown.add(song);
    }

    public void moveToHistory(RecommendedArtist artist){
        this.artistRecommendations.remove(artist);
        this.artistsShown.add(artist);
    }

    public void updateLists(){
        updateSongsList();
        updateArtistsList();
    }

    private void updateSongsList(){
        ArrayList<RecommendedSong> newList = new ArrayList<>();
        for(RecommendedSong song : songsRecommendations){
            if(song.wasShown()) this.songsShown.add(song);
            else newList.add(song);
        }
        this.songsRecommendations = newList;

    }

    private void updateArtistsList(){
        ArrayList<RecommendedArtist> newList = new ArrayList<>();
        for(RecommendedArtist artist: artistRecommendations) {
            if (artist.wasShown()) this.artistsShown.add(artist);
            else newList.add(artist);
        }
        this.artistRecommendations = newList;
    }


}
