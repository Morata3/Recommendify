package com.example.recommendify4.RecomThreads;

import com.example.recommendify4.SpotifyItems.Song;

import java.util.ArrayList;

public interface ContentCallback {
    void onComplete(ArrayList<Song> recommendations);

}
