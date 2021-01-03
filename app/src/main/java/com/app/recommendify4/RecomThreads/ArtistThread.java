package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.UserInfo.Credentials;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistThread implements Runnable{

    private String artist1,artist2,artist3;
    private String[] artistRecommended;
    private Credentials credentials;
    private ArtistCallback callback;

    public ArtistThread(String artist1,String artist2,String artist3,ArtistCallback callback, Credentials credentials){
        this.artist1 = artist1;
        this.artist2 = artist2;
        this.artist3 = artist3;
        this.callback = callback;
        this.credentials = credentials;
    }

    @Override
    public void run() {
        System.out.println("Artist thread started");
        System.out.println("BASE SONG: " + artist1 + ", "+artist2 + ", "+artist3);
        ArrayList<RecommendedArtist> arttistToRecommend = new ArrayList<>();

        Python py = Python.getInstance();
        PyObject pyf = py.getModule("3ArtistRecommender");
        PyObject obj= pyf.callAttr("recommend_artist", artist1, artist2, artist3);
        String recommenderOutput = obj.toString();

        artistRecommended = recommenderOutput.split(",");
        for (String artist : artistRecommended) {

            String recommendedArtistStr = RequestSender.searchArtistByName(artist, credentials);

            try{
                JSONObject possibleArtistJSON = new JSONObject(recommendedArtistStr);
                JSONObject artistInfo = possibleArtistJSON.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                RecommendedArtist recommendedArtist = new RecommendedArtist(artistInfo, 0);
                arttistToRecommend.add(recommendedArtist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        callback.onComplete(arttistToRecommend);
    }
}