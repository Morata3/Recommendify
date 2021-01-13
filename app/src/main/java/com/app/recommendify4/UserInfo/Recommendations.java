package com.app.recommendify4.UserInfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Recommendations implements Parcelable{

    private ArrayList<RecommendedSong> songsRecommendations;
    private ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> artistRecommendations;
    private ArrayList<RecommendedSong> hybridRecommendations;
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
        this.hybridRecommendations = new ArrayList<>();
        this.songsShown = new ArrayList<>();
        this.artistsShown = new ArrayList<>();
    }

    public ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> getArtistRecommendations() { return artistRecommendations; }

    public ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> getArtistsShown() { return artistsShown; }

    public ArrayList<RecommendedSong> getHybridRecommendations(){ return hybridRecommendations; }

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
        if(!this.songsShown.contains(song)) this.songsShown.add(song);
        checkHybrid();
    }

    public void moveToHistory(com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artist){
        this.artistRecommendations.remove(artist);
        if(!this.artistsShown.contains(artist)) this.artistsShown.add(artist);
        checkHybrid();
    }

    public boolean hasSongRecommendations(){
        return (songsRecommendations.size() > 0);
    }

    public boolean hasArtistRecommendations(){
        return (artistRecommendations.size() > 0);
    }

    private void checkHybrid(){
        if (this.artistRecommendations == null || this.artistRecommendations.size() == 0 ||
                this.songsRecommendations == null || this.songsRecommendations.size() == 0) {
        }
        else {
            for (RecommendedSong song : this.songsRecommendations) {
                for (com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artist : this.artistRecommendations) {
                    String[] SongGenres = song.getSongGenres();
                    ArrayList<String> ArtistGenres = artist.getGenres();
                    for (String songGenre : SongGenres) {
                        for (String artistgenre : ArtistGenres) {
                            if (songGenre.equals(artistgenre)) {
                                song.setCoincidence(song.getCoincidence() + 1);
                                hybridRecommendations.add(song);
                                System.out.println("COINCIDE!: " + song.getCoincidence());
                            }
                        }
                    }
                }
            }
            Collections.sort(this.hybridRecommendations, RecommendedSong.Coincidences);
        }
    }

    //PARCELABLE IMPLEMENTATION
    private Recommendations(Parcel in) {
        songsRecommendations = new ArrayList<>();
        artistRecommendations = new ArrayList<>();
        hybridRecommendations = new ArrayList<>();
        songsShown = new ArrayList<>();
        artistsShown = new ArrayList<>();

        in.readTypedList(songsRecommendations, RecommendedSong.CREATOR);
        in.readTypedList(artistRecommendations, RecommendedArtist.CREATOR);
        in.readTypedList(hybridRecommendations, RecommendedSong.CREATOR);
        in.readTypedList(songsRecommendations, RecommendedSong.CREATOR);
        in.readTypedList(artistRecommendations,RecommendedArtist.CREATOR);
    }

    public static final Parcelable.Creator<Recommendations> CREATOR
            = new Parcelable.Creator<Recommendations>() {
        public Recommendations createFromParcel(Parcel in) {
            return new Recommendations(in);
        }
        public Recommendations[] newArray(int size) {
            return new Recommendations[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(songsRecommendations);
        dest.writeTypedList(artistRecommendations);
        dest.writeTypedList(hybridRecommendations);
        dest.writeTypedList(songsShown);
        dest.writeTypedList(artistsShown);
    }

}
