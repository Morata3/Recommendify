package com.example.recommendify4.UserInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpotifyApiData {

    private static final String RECENTLY_PLAYED_ENDPOINT = "https://api.spotify.com/v1/me/player/recently-played";
    private static final String TOP_ARTISTS_AND_TRACKS_ENDPOINT = "https://api.spotify.com/v1/me/top";
    private static final int RECOVERED_SONGS = 50;
    private static final String TIME_RANGE = "medium_term";
    private static final int LIMIT = 50;
    private static final int OFFSET = 0;




    public static String getRecentlyPlayedSongs(String accessToken) {

        try {
            URL obj = new URL(RECENTLY_PLAYED_ENDPOINT + "?limit=" + RECOVERED_SONGS);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while((inputLine = responseReader.readLine()) != null) response.append(inputLine);
                return response.toString();
            }
            else{
                System.out.println("Codigo de respuesta: " + responseCode);
                return "ERROR";
            }
        } catch (Exception e) {
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
            return "ERROR";
        }

    }


    public static String getTopSongs(String accessToken) {

        try {
            URL obj = new URL(TOP_ARTISTS_AND_TRACKS_ENDPOINT + "/tracks?time_range=" + TIME_RANGE + "&limit=" + LIMIT + "&offset=" + OFFSET);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while((inputLine = responseReader.readLine()) != null) response.append(inputLine);
                return response.toString();
            }
            else{
                System.out.println("Codigo de respuesta: " + responseCode);
                return "ERROR";
            }
        } catch (Exception e) {
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
            return "ERROR";
        }

    }

    public static String getTopArtists(String accessToken) {

        try {
            URL obj = new URL(TOP_ARTISTS_AND_TRACKS_ENDPOINT + "/artists?time_range=" + TIME_RANGE + "&limit=" + LIMIT + "&offset=" + OFFSET);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
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
        } catch (Exception e) {
            System.out.println("Error creating JSONArray from received data");
            System.out.println(e.getMessage());
            return "ERROR";
        }

    }


}

