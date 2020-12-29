package com.app.recommendify4.SpotifyApi;

import com.app.recommendify4.SpotifyItems.Artist;
import com.app.recommendify4.SpotifyItems.Song;
import com.app.recommendify4.SpotifyItems.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseProcessor {

    public static User processUserInfoResponse(String response){
        try{
            if(!response.equals("ERROR")){
                JSONObject responseJSON = new JSONObject(response);
                JSONArray images_profile = responseJSON.getJSONArray("images");
                return new User(responseJSON,processImagesJSON(images_profile));
            }
            else return null;
        }catch(Exception e){
            System.out.println("Set user info: Error processing response");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Song> processRecentlyPlayedResponse(String response){
        try {
            if (!response.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(response);
                JSONArray recentlyPlayedSongs = reponseJson.getJSONArray("items");
                //processSongsJSON(recentlyPlayedSongs, this.recentlyPlayedSongs);
                return processSongsJSON(recentlyPlayedSongs);
            } else return null;
        }catch (Exception e){
            System.out.println("Set recently played songs: Error processing response");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Song> processTopSongsResponse(String response){
        try {
            if (!response.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(response);
                JSONArray topSongs = reponseJson.getJSONArray("items");
                return processSongsJSON(topSongs);
            } else return null;
        }catch (Exception e){
            System.out.println("Set top songs: Error processing response");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Artist> processTopArtistsResponse(String response, ArrayList<Song> topSongs){
        try{
            if (!response.equals("ERROR")) {
                JSONObject reponseJson = new JSONObject(response);
                JSONArray topArtists = reponseJson.getJSONArray("items");
                return processArtistsJSON(topArtists, topSongs);
            } else return null;
        }catch (Exception e){
            System.out.println("Set top artists: Error processing response");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Artist> processArtistsJSON(JSONArray artistsJSON, ArrayList<Song> topSongs) throws JSONException{
        ArrayList<Artist> list = new ArrayList<>();
        for(int index = 0; index < artistsJSON.length(); index++){
            JSONObject artistInfo = artistsJSON.getJSONObject(index);
            Artist artist = new Artist(artistInfo, 1);
            artist.setSongsInList(0);
            for(Song song : topSongs)
                if (song.getArtists().contains(artist))artist.addOneToSongsInList();
            list.add(artist);
        }
        return list;

    }

    private static ArrayList<Song> processSongsJSON(JSONArray songsJSON) throws JSONException{
        ArrayList<Song> list = new ArrayList<>();
        for(int index = 0; index < songsJSON.length(); index++){
            JSONObject songObject = songsJSON.getJSONObject(index);
            JSONObject songInfo;
            if(songObject.has("track")) songInfo = songObject.getJSONObject("track");
            else songInfo = songObject;
            Song song = new Song(songInfo, 1);
            if(!list.contains(song)) list.add(song);
            else list.get(list.indexOf(song)).addOneToTimesInList();
        }
        return list;

    }

    private static String processImagesJSON(JSONArray imagesJSON) throws JSONException {
        String imageURL = null;

        for(int index = 0; index < imagesJSON.length(); index ++){
            JSONObject imageObject = imagesJSON.getJSONObject(index);
            if(imageObject.has("url")) imageURL = imageObject.getString("url");
        }

        return imageURL;
    }

}

