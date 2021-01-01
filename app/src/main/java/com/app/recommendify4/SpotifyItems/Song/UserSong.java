package com.app.recommendify4.SpotifyItems.Song;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class UserSong extends Song implements Parcelable{

    private ArrayList<UserArtist> artists;
    private int timesInList;

    public UserSong(String songName, String albumName, ArrayList<UserArtist> artistsList, String songId, int timesInList){
        super(songName, albumName, songId);
        this.artists = artistsList;
        this.timesInList = timesInList;
    }

    public UserSong(JSONObject songInfo, int timesInList) throws JSONException {
        super(songInfo);
        this.artists = new ArrayList<>();
        JSONArray artistsJSON = songInfo.getJSONArray("artists");
        for(int artist = 0; artist < artistsJSON.length(); artist++)
            this.artists.add(new UserArtist(artistsJSON.getJSONObject(artist).getString("name"), artistsJSON.getJSONObject(artist).getString("id")));
        this.timesInList = timesInList;

    }

    public void setArtists(ArrayList<UserArtist> artists) { this.artists = artists; }

    public ArrayList<UserArtist> getArtists() { return artists; }

    public int getTimesInList() { return timesInList; }

    public void setTimesInList(int timesInList) { this.timesInList = timesInList; }

    public void incrementTimesInList(){ this.timesInList++; }


    @Override
    public String toString() {
        return "Song{" +
                "name='" + this.getName() + '\'' +
                ", album='" + this.getAlbum() + '\'' +
                ", id='" + this.getId() + '\'' +
                ", artists=" + artists +
                ", timesInList=" + timesInList +
                '}';
    }

    //PARCELABLE IMPLEMENTATION
    private UserSong(Parcel in) {
        artists = new ArrayList<>();
        this.setName(in.readString());
        this.setAlbum(in.readString());
        this.setId(in.readString());
        in.readTypedList(artists, UserArtist.CREATOR);
        timesInList = in.readInt();
    }

    public static final Parcelable.Creator<UserSong> CREATOR
            = new Parcelable.Creator<UserSong>() {
        public UserSong createFromParcel(Parcel in) {
            return new UserSong(in);
        }
        public UserSong[] newArray(int size) {
            return new UserSong[size];
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
        dest.writeInt(timesInList);
    }


}


