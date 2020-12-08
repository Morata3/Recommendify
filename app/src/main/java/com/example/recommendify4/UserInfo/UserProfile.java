package com.example.recommendify4.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class UserProfile {

    private static String accessToken;
    private ArrayList<Song> topSongs;
    private ArrayList<Artist> topArtists;
    private ArrayList<Song> recentlyPlayedSongs;
    private ArrayList<Artist> recentlyPlayedArtists;


    public UserProfile(String accessToken){
        this.accessToken = accessToken;
        recentlyPlayedSongs = new ArrayList<Song>();
        topSongs = new ArrayList<Song>();
        topArtists = new ArrayList<Artist>();
        recentlyPlayedArtists = new ArrayList<Artist>();
    }

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

    public void setRecentlyPlayedSongs() {

        try{
            String recentlyPlayedSongsAsString = SpotifyApiData.getRecentlyPlayedSongs(accessToken);
            if(!recentlyPlayedSongsAsString.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(recentlyPlayedSongsAsString);
                JSONArray recentlyPlayedSongs = reponseJson.getJSONArray("items");
                processSongsJSON(recentlyPlayedSongs, this.recentlyPlayedSongs);
            }
            else System.out.println("Something happened while trying to get recently played songs from API");

        }catch (Exception e){
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
        }

    }

    public void setRecentlyPlayedArtists(){

        for(Song song : recentlyPlayedSongs) {
            for (Artist artist : song.getArtists()) {
                if (!recentlyPlayedArtists.contains(artist)) recentlyPlayedArtists.add(artist);
                else artist.addOneToSongsInList();

            }
        }

    }

    public void setTopSongs() {

        try{
            String topSongsAsString = SpotifyApiData.getTopSongs(accessToken);
            if(!topSongsAsString.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(topSongsAsString);
                JSONArray topSongs = reponseJson.getJSONArray("items");
                processSongsJSON(topSongs, this.topSongs);
            }
            else System.out.println("Something happened while trying to get recently played songs from API");

        }catch (Exception e){
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
        }

    }

    public void setTopArtists() {

        try{
            String topArtistsAsString = SpotifyApiData.getTopArtists(accessToken);
            if(!topArtistsAsString.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(topArtistsAsString);
                JSONArray topArtists = reponseJson.getJSONArray("items");
                processArtistsJSON(topArtists, this.topArtists);
            }
            else System.out.println("Something happened while trying to get top artists from API");

        }catch (Exception e){
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
        }

    }

    private void processArtistsJSON(JSONArray artistsJSON, ArrayList<Artist> list){

        for(int index = 0; index < artistsJSON.length(); index++){
            try {
                JSONObject artistInfo = artistsJSON.getJSONObject(index);
                Artist artist = getArtistFromJSONObject(artistInfo);
                artist.setSongsInList(0);
                for(Song song : this.topSongs)
                    if (song.getArtists().contains(artist))artist.addOneToSongsInList();
                list.add(artist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void processSongsJSON(JSONArray songsJSON, ArrayList<Song> list){

        for(int index = 0; index < songsJSON.length(); index++){
            try {
                JSONObject songInfo = songsJSON.getJSONObject(index);
                Song song = getSongFromJSONObject(songInfo);
                if(!list.contains(song)) list.add(song);
                else list.get(list.indexOf(song)).addOneToTimesInList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private Artist getArtistFromJSONObject(JSONObject artistJSON) throws JSONException{
        ArrayList<String> artistGenres = new ArrayList<String>();
        String artistName = artistJSON.getString("name");
        String artistId = artistJSON.getString("id");

        JSONArray genresJSON = artistJSON.getJSONArray("genres");
        for(int genre = 0; genre < genresJSON.length(); genre++){
            artistGenres.add(genresJSON.getString(genre));
        }
        return new Artist(artistName, artistGenres, artistId);
    }

    private Song getSongFromJSONObject(JSONObject songJSON) throws JSONException{
        ArrayList<Artist> artistsList = new ArrayList<Artist>();
        String songName;
        String songId;
        String albumName;
        JSONArray artistsJSON;
        if(songJSON.has("track")){
            songName = songJSON.getJSONObject("track").getString("name");
            songId = songJSON.getJSONObject("track").getString("id");
            albumName = songJSON.getJSONObject("track").getJSONObject("album").getString("name");
            artistsJSON = songJSON.getJSONObject("track").getJSONArray("artists");
        }
        else{
            songName = songJSON.getString("name");
            songId = songJSON.getString("id");
            albumName = songJSON.getJSONObject("album").getString("name");
            artistsJSON = songJSON.getJSONArray("artists");
        }

        for(int artist = 0; artist < artistsJSON.length(); artist++){
            String artistName = artistsJSON.getJSONObject(artist).getString("name");
            String artistId = artistsJSON.getJSONObject(artist).getString("id");
            artistsList.add(new Artist(artistName, artistId));
        }
        return new Song(songName, albumName, artistsList, songId);
    }



}

