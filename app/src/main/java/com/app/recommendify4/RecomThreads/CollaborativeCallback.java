package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;

import java.util.ArrayList;

public interface CollaborativeCallback {
    void onComplete(ArrayList<RecommendedArtist> recommendations);

}
