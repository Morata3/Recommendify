package com.app.recommendify4.RecomThreads;

import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;

import java.util.ArrayList;

public interface ContentCallback {
    void onComplete(ArrayList<RecommendedSong> recommendations);

}
