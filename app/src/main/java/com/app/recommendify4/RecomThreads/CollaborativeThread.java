package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyItems.Artist;
import com.app.recommendify4.UserInfo.UserProfile;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class CollaborativeThread implements Runnable{

    private final Artist baseForRecommendations;
    private final CollaborativeCallback callback;
    private final UserProfile userProfile;

    public CollaborativeThread(Artist baseForRecommendations, CollaborativeCallback callback, UserProfile userProfile){
        this.baseForRecommendations = baseForRecommendations;
        this.callback = callback;
        this.userProfile= userProfile;
    }


    @Override
    public void run() {
        System.out.println("Collaborative thread started");

        ArrayList<Artist> recommendationsList = new ArrayList<>();

        Python py = Python.getInstance();
        PyObject pyf = py.getModule("AndroidArtistRecommender");
        PyObject obj = pyf.callAttr("recommend_artist", baseForRecommendations.getName());

        String recommendations = obj.toString();


        String[] recommended_artists = recommendations.split(",");

        for(int k = 0; k < recommended_artists.length; k++){

            //call the Spotify API to obtain data for the recommended artist
            String recommendedArtistStr = RequestSender.searchArtistByName(recommended_artists[k], userProfile.getCredentials());
            try {
                JSONObject possibleArtistJSON = new JSONObject(recommendedArtistStr);
                JSONObject artistInfo = possibleArtistJSON.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                Artist recommendedArtist = new Artist(artistInfo, 0);
                recommendationsList.add(recommendedArtist);
                System.out.println("BASE ARTIST: "+baseForRecommendations.getName()+"Recommended artist: "+recommendedArtist.getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("NUMBER OF RECOMMENDATIONS: " + recommendationsList.size() + ". FOR SONG: " + baseForRecommendations);
        callback.onComplete(recommendationsList);
    }

}