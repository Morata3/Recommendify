package com.example.recommendify4.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;


public class UserProfile {

    private static String accessToken;
    private JSONArray recentlyPlayedSongs;
    private JSONArray topArtists;
    private JSONArray topSongs;


    public UserProfile(String accessToken){
        this.accessToken = accessToken;
        recentlyPlayedSongs = new JSONArray();
    }

    public JSONArray getRecentlyPlayedSongs() {
        return recentlyPlayedSongs;
    }

    public JSONArray getTopArtists() {
        return topArtists;
    }

    public JSONArray getTopSongs() {
        return topSongs;
    }

    public void setRecentlyPlayedSongs() {

        try{
            String recentlyPlayedSongsAsString = SpotifyApiData.getRecentlyPlayedSongs(accessToken);
            if(!recentlyPlayedSongsAsString.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(recentlyPlayedSongsAsString);
                this.recentlyPlayedSongs = reponseJson.getJSONArray("items");
            }
            else System.out.println("Something happened while trying to get recently played songs from API");

        }catch (Exception e){
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
        }

    }

    public void setTopSongs() {

        try{
            String topSongsAsString = SpotifyApiData.getTopSongs(accessToken);
            if(!topSongsAsString.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(topSongsAsString);
                this.topSongs = reponseJson.getJSONArray("items");
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
                this.topArtists = reponseJson.getJSONArray("items");
            }
            else System.out.println("Something happened while trying to get top artists from API");

        }catch (Exception e){
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
        }

    }



}

