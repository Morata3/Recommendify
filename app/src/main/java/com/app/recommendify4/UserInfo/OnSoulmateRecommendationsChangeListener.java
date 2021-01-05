package com.app.recommendify4.UserInfo;

import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;

import java.util.ArrayList;

public interface OnSoulmateRecommendationsChangeListener {
    void onSoulmateRecommendationsChanged(ArrayList<RecommendedArtist> artistRecommendations);
}
