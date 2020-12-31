package com.app.recommendify4.SpotifyItems.Song;

import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class UserSong extends Song{

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


}


