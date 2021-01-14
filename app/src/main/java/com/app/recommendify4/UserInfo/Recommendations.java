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
        setSongCoincidences(song);
        if(song.getCoincidence() > 0 && !hybridRecommendations.contains(song)){
            hybridRecommendations.add(song);
            Collections.sort(this.hybridRecommendations, RecommendedSong.Coincidences);
        }
        this.songsRecommendations.remove(song);
        if(!this.songsShown.contains(song)) this.songsShown.add(song);
    }

    public void moveToHistory(RecommendedArtist artist){
        updateSongsCoincidences(artist);
        this.artistRecommendations.remove(artist);
        if(!this.artistsShown.contains(artist))
            this.artistsShown.add(artist);

        //checkHybrid();
    }

    public boolean hasSongRecommendations(){
        return (songsRecommendations.size() > 0);
    }

    public boolean hasArtistRecommendations(){
        return (artistRecommendations.size() > 0);
    }


    private void setSongCoincidences(RecommendedSong song){
        String[] songGenres = song.getSongGenres();
        for(RecommendedArtist shownArtist : this.artistsShown){
            ArrayList<String> artistGenres = shownArtist.getGenres();
            for(String artistGenre : artistGenres){
                for(String songGenre : songGenres){
                    if(songGenre.equals(artistGenre)){
                        System.out.println("Song: " +  song.toString() + " contains genre " + songGenre + " like the artist " + shownArtist.getName());
                        song.setCoincidence(song.getCoincidence() + 1);
                    }
                }
            }
        }

    }

    private void updateSongsCoincidences(RecommendedArtist artist){
        ArrayList<String> artistGenres = artist.getGenres();
        for(RecommendedSong hybridSong : this.hybridRecommendations){
            String[] songGenres = hybridSong.getSongGenres();
            for(String songGenre : songGenres){
                if(artistGenres.contains(songGenre)){
                    System.out.println("Song: " +  hybridSong.getName() + " contains genre " + songGenre + " like the artist " + artist.getName());
                    hybridSong.setCoincidence(hybridSong.getCoincidence() + 1);
                }
            }
        }
        for(RecommendedSong shownSong : this.songsShown){
            String[] songGenres = shownSong.getSongGenres();
            for(String songGenre : songGenres){
                if(artistGenres.contains(songGenre)){
                    System.out.println("Song: " +  shownSong.getName() + " contains genre " + songGenre + " like the artist " + artist.getName());
                    shownSong.setCoincidence(shownSong.getCoincidence() + 1);
                }
            }
        }
        for(RecommendedSong shownSong : this.songsShown)
            if(shownSong.getCoincidence() > 0 && !this.hybridRecommendations.contains(shownSong))
                this.hybridRecommendations.add(shownSong);

        Collections.sort(this.hybridRecommendations, RecommendedSong.Coincidences);

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
        in.readTypedList(songsShown, RecommendedSong.CREATOR);
        in.readTypedList(artistsShown,RecommendedArtist.CREATOR);
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
