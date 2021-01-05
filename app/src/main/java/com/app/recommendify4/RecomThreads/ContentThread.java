package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyApi.ResponseProcessor;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.UserSong;
import com.app.recommendify4.ThreadManagers.ThreadLauncher;
import com.app.recommendify4.UserInfo.Credentials;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ContentThread implements Runnable{

    private final UserSong baseForRecommendations;
    private final ContentCallback callback;
    private final Credentials credentials;

    public ContentThread(UserSong baseForRecommendations, ContentCallback callback, Credentials credentials){
        this.baseForRecommendations = baseForRecommendations;
        this.callback = callback;
        this.credentials = credentials;
    }


    @Override
    public void run() {
        System.out.println("Content thread started");
        System.out.println("BASE SONG: " + baseForRecommendations);
        ArrayList<RecommendedSong> recommendationsList = new ArrayList<>();
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("FinalRecomendator");

        PyObject obj = pyf.callAttr("rank_song_similarity_by_measure", baseForRecommendations.toString(), 2);
        String recommendations = obj.toString();
        JSONArray jsonrecom = null;
        try{
            jsonrecom= new JSONArray(recommendations);
        }catch(JSONException e){
            e.printStackTrace();
        }
        String title = null;
        String artist = null;
        String id = null;
        String Genres = null;

        for(int index = 1; index < jsonrecom.length(); index++){

            try {
                if(!(jsonrecom.getJSONObject(index).getString("song_name").length() == 1))
                {

                    title = jsonrecom.getJSONObject(index).getString("song_name");
                    artist = jsonrecom.getJSONObject(index).getString("artist");
                    id = jsonrecom.getJSONObject(index).getString("id");
                    Genres = jsonrecom.getJSONObject(index).getString("genres");
                    RecommendedSong recommendedSong = new RecommendedSong(title, artist, id, 0,Genres);
                    ThreadLauncher builder_updateTrack = new ThreadLauncher();
                    builder_updateTrack.execute(new Runnable() {
                        @Override
                        public void run() {
                            String response = RequestSender.getTrackInfo(credentials,recommendedSong.getId());
                            ResponseProcessor.processTrackResponse(response,recommendedSong);
                        }
                    });
                    recommendationsList.add(recommendedSong);
                    System.out.println("(DEBUG_MESAGE) BASE SONG: "+ baseForRecommendations.getName()+" --> Recommended song: "+recommendedSong.getName()+" - "+recommendedSong.getArtists().toString() +" - Genres:"+recommendedSong.getGenres());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        //System.out.println("NUMBER OF RECOMMENDATIONS: " + recommendationsList.size() + ". FOR SONG: " + baseForRecommendations);
        callback.onComplete(recommendationsList);
    }

}



