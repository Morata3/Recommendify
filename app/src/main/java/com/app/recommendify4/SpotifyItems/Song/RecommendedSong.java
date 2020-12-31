package com.app.recommendify4.SpotifyItems.Song;

import android.os.Parcel;
import android.os.Parcelable;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import java.util.ArrayList;

public class RecommendedSong extends Song implements Parcelable {
    private ArrayList<RecommendedArtist> artists;
    private int shown;
    private String coverURL;
    private String previewURL;

    public RecommendedSong(String songName, String albumName, String songId, ArrayList<RecommendedArtist> artists, int shown){
        super(songName, albumName, songId);
        this.artists = artists;
        this.shown = shown;
    }

    public RecommendedSong(String songName, String artistsList, String songId, int shown){
        super(songName, songId);
        this.artists = getArtistsFromString(artistsList);
        this.shown = shown;
    }

    public void setArtists(ArrayList<RecommendedArtist> artists) { this.artists = artists; }

    public ArrayList<RecommendedArtist> getArtists() { return artists; }

    public String getCoverURL(){return coverURL; }

    public String getPreviewURL(){ return previewURL; }

    public void setCoverURL(String url) { this.coverURL = url; }

    public void setPreviewURL(String url)  {this.previewURL = url; }

    public boolean wasShown() { return shown != 0; }

    public void setShown(int shown) { this.shown = shown; }

    private ArrayList<RecommendedArtist> getArtistsFromString(String artistList){
        ArrayList<RecommendedArtist> list = new ArrayList<>();
        String[] names = artistList.split(",");
        for(String name : names){
            RecommendedArtist artist = new RecommendedArtist(name);
            list.add(artist);
        }
        return list;
    }

    //PARCELABLE IMPLEMENTATION
    private RecommendedSong(Parcel in) {
        artists = new ArrayList<>();
        this.setName(in.readString());
        this.setAlbum(in.readString());
        this.setId(in.readString());
        in.readTypedList(artists, RecommendedArtist.CREATOR);
        shown = in.readInt();
    }

    public static final Parcelable.Creator<Song> CREATOR
            = new Parcelable.Creator<Song>() {
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
        dest.writeString(this.getAlbum());
        dest.writeTypedList(artists);
        dest.writeInt(shown);
    }
}
