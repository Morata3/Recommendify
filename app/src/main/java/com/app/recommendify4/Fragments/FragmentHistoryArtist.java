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
import com.app.recommendify4.SpotifyItems.Artist.ArtistAdapter;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.SongAdapter;

import java.util.ArrayList;


public class FragmentHistoryArtist extends Fragment {

    private static final String ARTIST = "artists";

    private ArrayList<RecommendedArtist> artists;
    private ListView listView;


    public FragmentHistoryArtist() {
        // Required empty public constructor
    }

    public static FragmentHistoryArtist newInstance(ArrayList<RecommendedArtist> artists) {
        FragmentHistoryArtist fragment = new FragmentHistoryArtist();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTIST,artists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artists = getArguments().getParcelableArrayList(ARTIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(artists != null ){
            ArtistAdapter adapter = new ArtistAdapter(getActivity(),R.layout.item_artist,artists);
            listView = view.findViewById(R.id.listArtistView);
            listView.setAdapter(adapter);
        }else System.out.println("No artist on history");

    }
}