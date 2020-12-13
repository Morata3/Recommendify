package com.example.recommendify4.SpotifyApi;

import com.example.recommendify4.SpotifyItems.Song;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RequestSender {

    private static final String RECENTLY_PLAYED_ENDPOINT = "https://api.spotify.com/v1/me/player/recently-played";
    private static final String TOP_ARTISTS_AND_TRACKS_ENDPOINT = "https://api.spotify.com/v1/me/top";
    private static final String SEARCH_ENDPOINT = "https://api.spotify.com/v1/search";
    private static final String USER_INFO_ENDPOINT = "https://api.spotify.com/v1/me";
    private static final String CREATE_PLAYLIST_ENDPOINT = "https://api.spotify.com/v1/users/";
    private static final String ADD_SONGS_TO_PLAYLIST_ENDPOINT = "https://api.spotify.com/v1/playlists/";

    private static final int RECOVERED_SONGS = 50;
    private static final String TIME_RANGE = "long_term";
    private static final int LIMIT = 50;
    private static final int OFFSET = 0;

    public static String getUserInfo(String accessToken){
        try {
            URL obj = new URL(USER_INFO_ENDPOINT);
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Search Artist By Name: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String createPlaylist(String accessToken, String playlistName, String user_id){

        try {
            String payloadAsString = "{\"name\": \"" + playlistName + "\"}";
            byte[] payload = payloadAsString.getBytes("utf-8");
            URL obj = new URL(CREATE_PLAYLIST_ENDPOINT + user_id + "/playlists");
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(payload);
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Create Playlist: Error creating playlist");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String addSongsToPlaylist(String accessToken, ArrayList<Song> songs, String playlist_id){

        try {
            StringBuilder payloadBuilder = new StringBuilder();
            payloadBuilder.append("{\"uris\": [");
            for(int index = 0; index < songs.size(); index++){
                if(index == songs.size() - 1) payloadBuilder.append("\"spotify:track:").append(songs.get(index).getId()).append("\"]}");
                else payloadBuilder.append("\"spotify:track:").append(songs.get(index).getId()).append("\",");
            }
            String payloadAsString = payloadBuilder.toString();
            byte[] payload = payloadAsString.getBytes("utf-8");
            URL obj = new URL(ADD_SONGS_TO_PLAYLIST_ENDPOINT + playlist_id + "/tracks");
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "POST");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(payload);
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Create Playlist: Error creating playlist");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }


    public static String searchArtistByName(String artistName, String accessToken) {

        String artistNameEncoded = artistName.replace(" ", "%20");
        try {
            URL obj = new URL(SEARCH_ENDPOINT + "?q=" + artistNameEncoded + "&type=artist");
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Search Artist By Name: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }

    }


    public static String getRecentlyPlayedSongs(String accessToken) {

        try {
            URL obj = new URL(RECENTLY_PLAYED_ENDPOINT + "?limit=" + RECOVERED_SONGS);
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Get Recently Played Songs: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }

    }


    public static String getTopSongs(String accessToken) {

        try {
            URL obj = new URL(TOP_ARTISTS_AND_TRACKS_ENDPOINT + "/tracks?time_range=" + TIME_RANGE + "&limit=" + LIMIT + "&offset=" + OFFSET);
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Get Top Songs: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";

        }

    }

    public static String getTopArtists(String accessToken) {

        try {
            URL obj = new URL(TOP_ARTISTS_AND_TRACKS_ENDPOINT + "/artists?time_range=" + TIME_RANGE + "&limit=" + LIMIT + "&offset=" + OFFSET);
            HttpURLConnection con = buildHttpRequest(accessToken, obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Get Top Artists: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }

    }

    private static HttpURLConnection buildHttpRequest(String accessToken, URL url, String method) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        return con;
    }

    private static String getResponseFromApi(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while((inputLine = responseReader.readLine()) != null) response.append(inputLine);
            return response.toString();
        }
        else{
            System.out.println("Codigo de respuesta: " + responseCode);
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while((inputLine = responseReader.readLine()) != null) System.out.println(inputLine);
            return "ERROR";

        }
    }


}


