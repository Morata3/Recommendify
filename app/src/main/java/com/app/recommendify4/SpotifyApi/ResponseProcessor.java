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
                return new User(responseJSON,processUserImageJSON(images_profile));
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

    public static void processTrackResponse(String response, Song song){
        try{
            if (!response.equals("ERROR")) {
                JSONObject responseJson = new JSONObject(response);
                processTrackJSON(responseJson,song);
            }

        }catch (Exception e){
            System.out.println("Set track info: Error processing response");
            System.out.println(e.getMessage());
            e.printStackTrace();

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

    private static void processTrackJSON(JSONObject trackJSON, Song song) throws JSONException {
        JSONObject albumInfo;

        if(trackJSON.has("album")){
            albumInfo = trackJSON.getJSONObject("album");
            song.setCoverURL(processCoverAlbum(albumInfo));
        }
        if(trackJSON.has("preview_url")) song.setPrewviewURL(trackJSON.getString("preview_url"));
        System.out.println("PREVIEW: " + song.getId());
    }

    private static String processCoverAlbum(JSONObject albumCoverJSON) throws JSONException{
        String coverURL = null;

        JSONArray images;
        if(albumCoverJSON.has("images")){
            images = albumCoverJSON.getJSONArray("images");
            for(int index=0; index < images.length(); index ++){
                JSONObject imageObject = images.getJSONObject(index);
                if(imageObject.has("url")){
                    if(imageObject.getInt("height") == 300) coverURL = imageObject.getString("url");
                }
            }
        }

        return coverURL;
    }

    private static String processUserImageJSON(JSONArray userImagesJSON) throws JSONException {
        String imageURL = null;

        for(int index = 0; index < userImagesJSON.length(); index ++){
            JSONObject imageObject = userImagesJSON.getJSONObject(index);
            if(imageObject.has("url")) imageURL = imageObject.getString("url");
        }
        return imageURL;
    }

}

