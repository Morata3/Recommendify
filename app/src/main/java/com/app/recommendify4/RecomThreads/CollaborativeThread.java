package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.UserInfo.Credentials;
import com.app.recommendify4.UserInfo.UserProfile;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class CollaborativeThread implements Runnable{

    private final UserArtist baseForRecommendations;
    private final CollaborativeCallback callback;
    private final Credentials credentials;


    public CollaborativeThread(UserArtist baseForRecommendations, CollaborativeCallback callback, Credentials credentials){
        this.baseForRecommendations = baseForRecommendations;
        this.callback = callback;
        this.credentials= credentials;
    }


    @Override
    public void run() {
        System.out.println("Collaborative thread started");

        ArrayList<RecommendedArtist> recommendationsList = new ArrayList<>();

        Python py = Python.getInstance();
        PyObject pyf = py.getModule("AndroidArtistRecommender");
        PyObject obj = pyf.callAttr("recommend_artist", baseForRecommendations.getName());

        String recommenderOutput = obj.toString();

        if(!recommenderOutput.startsWith("(ERROR):")) {

            String[] recommended_artists = recommenderOutput.split(",");

            for (int k = 0; k < recommended_artists.length; k++) {

                //call the Spotify API to obtain data for the recommended artist
                String recommendedArtistStr = RequestSender.searchArtistByName(recommended_artists[k], credentials);
                try {
                    JSONObject possibleArtistJSON = new JSONObject(recommendedArtistStr);
                    JSONObject artistInfo = possibleArtistJSON.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                    RecommendedArtist recommendedArtist = new RecommendedArtist(artistInfo, 0);
                    recommendationsList.add(recommendedArtist);
                    System.out.println("BASE ARTIST: " + baseForRecommendations.getName() + " --> Recommended artist: " + recommendedArtist.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println("NUMBER OF RECOMMENDATIONS: " + recommendationsList.size() + ". FOR SONG: " + baseForRecommendations);
            callback.onComplete(recommendationsList);
        }else
            System.out.println(recommenderOutput);
    }

}