package com.app.recommendify4.SpotifyItems.Song;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

public class RecommendedSong extends Song implements Parcelable {
    private ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> artists;
    private String genres;
    private String coverURL;
    private int Coincidence;
    private String previewURL;

    public RecommendedSong(String songName, String albumName, String songId, ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> artists, int shown){
        super(songName, albumName, songId);
        this.artists = artists;
        this.Coincidence = 0;
    }

    public RecommendedSong(String songName, String artistsList, String songId, int shown, String Genres){
        super(songName, songId);
        this.artists = getArtistsFromString(artistsList);
        this.Coincidence = 0;
        this.genres = Genres;
    }

    public void setArtists(ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> artists) { this.artists = artists; }

    public ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> getArtists() { return artists; }

    public String getCoverURL(){return coverURL; }

    public String getPreviewURL(){ return previewURL; }

    public void setCoverURL(String url) { this.coverURL = url; }

    public void setPreviewURL(String url)  {this.previewURL = url; }

    public int getCoincidence(){return Coincidence; }

    public void setGenres(String genres){this.genres = genres;}

    public String getGenres(){return this.genres;}

    public void setCoincidence(int Coincidence){this.Coincidence = Coincidence;}

    private ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> getArtistsFromString(String artistList){
        ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> list = new ArrayList<>();
        String[] names = artistList.split(",");
        for(String name : names){
            com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artist = new com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist(name);
            list.add(artist);
        }
        return list;
    }

    public String[] getSongGenres(){
        String str = this.genres;
        str = str.replace("\"","");
        str = str.replace("\'","");
        str = str.replace("[","");
        str = str.replace("]","");
        str = str.trim();
        String[] songGenres = str.split(", ");
        return songGenres;
    }

    //PARCELABLE IMPLEMENTATION
    private RecommendedSong(Parcel in) {
        artists = new ArrayList<>();
        this.setName(in.readString());
        this.setAlbum(in.readString());
        this.setId(in.readString());
        this.setCoverURL(in.readString());
        this.setGenres(in.readString());
        this.setPreviewURL(in.readString());
        this.setCoincidence(in.readInt());

        in.readTypedList(artists, com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist.CREATOR);
    }

    public static final Parcelable.Creator<RecommendedSong> CREATOR
            = new Parcelable.Creator<RecommendedSong>() {
        public RecommendedSong createFromParcel(Parcel in) {
            return new RecommendedSong(in);
        }
        public RecommendedSong[] newArray(int size) {
            return new RecommendedSong[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getName());
        dest.writeString(this.getAlbum());
        dest.writeString(this.getId());
        dest.writeString(this.getCoverURL());
        dest.writeString(this.getGenres());
        dest.writeString(this.getPreviewURL());
        dest.writeInt(this.getCoincidence());

        dest.writeTypedList(artists);
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + getName() + '\'' +
                ", album='" + getAlbum() + '\'' +
                ", id='" + getId() + '\'' +
                ", coincidences='" + Coincidence + '\'' +
                ", image="+ coverURL+

        '}';
    }


    public static Comparator<RecommendedSong> Coincidences = new Comparator<RecommendedSong>() {

        public int compare(RecommendedSong s1, RecommendedSong s2) {

            int song1 = s1.getCoincidence();
            int song2 = s2.getCoincidence();

            return song2-song1;

        }
    };

}