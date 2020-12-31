package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;

public class ArtistThread implements Runnable{

    private String artist1,artist2,artist3;
    private String[] artistRecommended;
    private ArtistCallback callback;

    public ArtistThread(String artist1,String artist2,String artist3,ArtistCallback callback){
        this.artist1 = artist1;
        this.artist2 = artist2;
        this.artist3 = artist3;
        this.callback = callback;
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
            arttistToRecommend.add(new RecommendedArtist(artist));
        }

        callback.onComplete(arttistToRecommend);
    }
}