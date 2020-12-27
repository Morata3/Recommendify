package com.example.recommendify4.SpotifyApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.example.recommendify4.Login;
import com.example.recommendify4.SpotifyItems.Song;
import com.example.recommendify4.UserInfo.Credentials;
import com.example.recommendify4.ThreadLauncher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class RequestSender {

    private static final String RECENTLY_PLAYED_ENDPOINT = "https://api.spotify.com/v1/me/player/recently-played";
    private static final String TOP_ARTISTS_AND_TRACKS_ENDPOINT = "https://api.spotify.com/v1/me/top";
    private static final String SEARCH_ENDPOINT = "https://api.spotify.com/v1/search";
    private static final String USER_INFO_ENDPOINT = "https://api.spotify.com/v1/me";
    private static final String CREATE_PLAYLIST_ENDPOINT = "https://api.spotify.com/v1/users/";
    private static final String ADD_SONGS_TO_PLAYLIST_ENDPOINT = "https://api.spotify.com/v1/playlists/";
    private static final String TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token";

    private static final int RECOVERED_SONGS = 50;
    private static final String TIME_RANGE = "long_term";
    private static final int LIMIT = 50;
    private static final int OFFSET = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getUserInfo(Credentials credentials){
        credentials.checkTokenExpiration();
        try {
            URL obj = new URL(USER_INFO_ENDPOINT);
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Search Artist By Name: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String createPlaylist(Credentials credentials, String playlistName, String user_id){
        credentials.checkTokenExpiration();
        try {
            String payloadAsString = "{\"name\": \"" + playlistName + "\"}";
            byte[] payload = payloadAsString.getBytes("utf-8");
            URL obj = new URL(CREATE_PLAYLIST_ENDPOINT + user_id + "/playlists");
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "POST");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String addSongsToPlaylist(Credentials credentials, ArrayList<Song> songs, String playlist_id){
        credentials.checkTokenExpiration();
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
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "POST");
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String searchArtistByName(String artistName, Credentials credentials) {
        credentials.checkTokenExpiration();
        String artistNameEncoded = artistName.replace(" ", "%20");
        try {
            URL obj = new URL(SEARCH_ENDPOINT + "?q=" + artistNameEncoded + "&type=artist");
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Search Artist By Name: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRecentlyPlayedSongs(Credentials credentials) {
        credentials.checkTokenExpiration();
        try {
            URL obj = new URL(RECENTLY_PLAYED_ENDPOINT + "?limit=" + RECOVERED_SONGS);
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Get Recently Played Songs: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getTopSongs(Credentials credentials) {
        credentials.checkTokenExpiration();
        try {
            URL obj = new URL(TOP_ARTISTS_AND_TRACKS_ENDPOINT + "/tracks?time_range=" + TIME_RANGE + "&limit=" + LIMIT + "&offset=" + OFFSET);
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "GET");
            return getResponseFromApi(con);
        } catch (Exception e) {
            System.out.println("Get Top Songs: Error receiving data from Spotify Api");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "ERROR";

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getTopArtists(Credentials credentials) {
        credentials.checkTokenExpiration();
        try {
            URL obj = new URL(TOP_ARTISTS_AND_TRACKS_ENDPOINT + "/artists?time_range=" + TIME_RANGE + "&limit=" + LIMIT + "&offset=" + OFFSET);
            HttpURLConnection con = buildHttpRequest(credentials.getAcces_token(), obj, "GET");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) //Fai falta para o StandardCharset.UTF-8
    public static String getTokens(String AuthorizationCode, String cliend_id, String client_secret, String redirect_URL){
        try {
            String app_credentials = cliend_id + ":" + client_secret;
            byte[] toEncode = app_credentials.getBytes("UTF-8");
            String urlParameters = "grant_type=authorization_code&code=" + AuthorizationCode + "&redirect_uri=" + redirect_URL;
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;

            URL obj = new URL(TOKEN_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput( true );
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization","Basic " + Base64.encodeToString(toEncode, Base64.NO_WRAP));
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
                wr.write( postData );
            }

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while((inputLine = responseReader.readLine()) != null) response.append(inputLine);
                return response.toString();
            }
            else{
                System.out.println("[getTokens] Codigo de respuesta: " + responseCode);
                return "ERROR";
            }
        } catch (Exception e) {
            System.out.println("Error getting TOKEN from spotify api");
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "ERROR";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getAccessToken(String refresh_token, String cliend_id, String client_secret){
        try {
            String app_credentials = cliend_id + ":" + client_secret;
            byte[] toEncode = app_credentials.getBytes("UTF-8");
            String urlParameters = "grant_type=refresh_token&refresh_token=" + refresh_token;
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;

            URL obj = new URL(TOKEN_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput( true );
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization","Basic " + Base64.encodeToString(toEncode, Base64.NO_WRAP));
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
                wr.write( postData );
            }

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while((inputLine = responseReader.readLine()) != null) response.append(inputLine);

                return response.toString();
            }
            else{
                System.out.println("[getAccessToken] Codigo de respuesta : " + responseCode);
                return "ERROR";
            }
        } catch (Exception e) {
            System.out.println("Error getting TOKEN from spotify api");
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "ERROR";
        }
    }

}


