package com.app.recommendify4.UserInfo;

import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;

import java.util.ArrayList;

public interface OnShuffleRecommendationsChangeListener {
    void onShuffleRecommendationsChanged(ArrayList<RecommendedSong> songRecommendations);
}