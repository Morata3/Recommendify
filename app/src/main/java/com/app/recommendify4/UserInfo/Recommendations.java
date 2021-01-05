package com.app.recommendify4.UserInfo;

import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;

import java.util.ArrayList;

public class Recommendations {

    private ArrayList<RecommendedSong> songsRecommendations;
    private ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> artistRecommendations;

    private ArrayList<RecommendedSong> songsShown;
    private ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> artistsShown;

    private OnSoulmateRecommendationsChangeListener artistListener;
    private OnShuffleRecommendationsChangeListener songListener;

    public void setOnSoulmateRecommendationsChangeListener(OnSoulmateRecommendationsChangeListener listener)
    {
        this.artistListener = listener;
    }

    public void setOnShuffleRecommendationsChangeListener(OnShuffleRecommendationsChangeListener listener){
        this.songListener=listener;
    }

    public Recommendations(){
        this.songsRecommendations = new ArrayList<>();
        this.artistRecommendations = new ArrayList<>();
        this.songsShown = new ArrayList<>();
        this.artistsShown = new ArrayList<>();
    }

    public ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> getArtistRecommendations() { return artistRecommendations; }

    public ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> getArtistsShown() { return artistsShown; }

    public ArrayList<RecommendedSong> getSongsRecommendations() { return songsRecommendations; }

    public ArrayList<RecommendedSong> getSongsShown() { return songsShown; }

    public void addSongRecommendations(ArrayList<RecommendedSong> newRecommendations){
        this.songsRecommendations.addAll(newRecommendations);
        if(songListener != null)
        {
            songListener.onShuffleRecommendationsChanged(songsRecommendations);
        }
    }

    public void addArtistRecommendations(ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> newRecommendations){
        this.artistRecommendations.addAll(newRecommendations);
        if(artistListener != null)
        {
            artistListener.onSoulmateRecommendationsChanged(artistRecommendations);
        }
    }

    public void moveToHistory(RecommendedSong song){
        this.songsRecommendations.remove(song);
        this.songsShown.add(song);
    }

    public void moveToHistory(com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artist){
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
        ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> newList = new ArrayList<>();
        for(com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artist: artistRecommendations) {
            if (artist.wasShown()) this.artistsShown.add(artist);
            else newList.add(artist);
        }
        this.artistRecommendations = newList;
    }


}
