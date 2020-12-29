package com.example.recommendify4.RecomThreads;

import com.example.recommendify4.SpotifyItems.Artist;

import java.util.ArrayList;

public interface CollaborativeCallback {
    void onComplete(ArrayList<Artist> recommendations);

}
