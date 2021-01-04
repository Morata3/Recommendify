package com.app.recommendify4.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.SongAdapter;

import java.util.ArrayList;


public class FragmentHistorySong extends Fragment {

    private ArrayList<RecommendedSong> songs;
    private ListView listView;

    private static final String SONG = "song";

    public FragmentHistorySong() {
        // Required empty public constructor
    }


    public static FragmentHistorySong newInstance(ArrayList<RecommendedSong> songs) {
        FragmentHistorySong fragment = new FragmentHistorySong();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SONG,songs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songs = getArguments().getParcelableArrayList(SONG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(songs != null ){
            System.out.println("(FragmentHistorySong): "+songs.get(0));
            SongAdapter adapter = new SongAdapter(getActivity(),R.layout.item_song,songs);
            listView = view.findViewById(R.id.listSongView);
            listView.setAdapter(adapter);
        }else System.out.println("No songs on history");

    }
}
