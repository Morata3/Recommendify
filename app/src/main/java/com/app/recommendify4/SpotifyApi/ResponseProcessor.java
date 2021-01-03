package com.app.recommendify4.SpotifyApi;

import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.UserSong;
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

    public static ArrayList<UserSong> processRecentlyPlayedResponse(String response){
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

    public static ArrayList<UserSong> processTopSongsResponse(String response){
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

    public static ArrayList<UserArtist> processTopArtistsResponse(String response, ArrayList<UserSong> topSongs){
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

    public static void processTrackResponse(String response, RecommendedSong song){
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

    private static ArrayList<UserArtist> processArtistsJSON(JSONArray artistsJSON, ArrayList<UserSong> topSongs) throws JSONException{
        ArrayList<UserArtist> list = new ArrayList<>();
        for(int index = 0; index < artistsJSON.length(); index++){
            JSONObject artistInfo = artistsJSON.getJSONObject(index);
            UserArtist artist = new UserArtist(artistInfo, 1);
            artist.setSongsInList(0);
            for(UserSong song : topSongs)
                if (song.getArtists().contains(artist))artist.incrementSongsInList();
            list.add(artist);
        }
        return list;

    }

    private static ArrayList<UserSong> processSongsJSON(JSONArray songsJSON) throws JSONException{
        ArrayList<UserSong> list = new ArrayList<>();
        for(int index = 0; index < songsJSON.length(); index++){
            JSONObject songObject = songsJSON.getJSONObject(index);
            JSONObject songInfo;
            if(songObject.has("track")) songInfo = songObject.getJSONObject("track");
            else songInfo = songObject;
            UserSong song = new UserSong(songInfo, 1);
            if(!list.contains(song)) list.add(song);
            else list.get(list.indexOf(song)).incrementTimesInList();
        }
        return list;

    }

    private static void processTrackJSON(JSONObject trackJSON, RecommendedSong song) throws JSONException {
        JSONObject albumInfo;

        if(trackJSON.has("album")){
            albumInfo = trackJSON.getJSONObject("album");
            song.setCoverURL(processCoverAlbum(albumInfo));
        }
        if(trackJSON.has("preview_url")) song.setPreviewURL(trackJSON.getString("preview_url"));
    }

    private static String processCoverAlbum(JSONObject albumCoverJSON) throws JSONException{
        String coverURL = null;

        JSONArray images;
        if(albumCoverJSON.has("images")){
            images = albumCoverJSON.getJSONArray("images");
            for(int index=0; index < images.length(); index ++){
                JSONObject imageObject = images.getJSONObject(index);
                if(imageObject.has("url")){
                    if(imageObject.getInt("height") >= 250) coverURL = imageObject.getString("url");
                }
            }
        }

        return coverURL;
    }

    public static String processArtistImage(JSONObject artistImageJSON) throws JSONException{
        String artistImageURL = null;

        JSONArray images;
        if(artistImageJSON.has("images")){
            images = artistImageJSON.getJSONArray("images");
            for(int index=0; index < images.length(); index ++){
                JSONObject imageObject = images.getJSONObject(index);
                if(imageObject.has("url")){
                    if(imageObject.getInt("height") >= 250) artistImageURL = imageObject.getString("url");
                }
            }
        }

        return artistImageURL;
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

