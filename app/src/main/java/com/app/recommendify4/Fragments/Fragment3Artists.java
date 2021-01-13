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

import java.util.ArrayList;


public class Fragment3Artists extends Fragment {

    private ArrayList<RecommendedArtist> recommendedArtists;
    private ArrayList<RecommendedArtist> artistShown;
    private ListView listView;

    private static final String ARTISTS = "artists";


    public Fragment3Artists() {
        // Required empty public constructor
    }


    public static Fragment3Artists newInstance(ArrayList<RecommendedArtist> artists) {
        Fragment3Artists fragment = new Fragment3Artists();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTISTS,artists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recommendedArtists = getArguments().getParcelableArrayList(ARTISTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_3artists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArtistAdapter adapter = new ArtistAdapter(getActivity(),R.layout.item_artist_3recommender,recommendedArtists);
        listView = view.findViewById(R.id.listArtistView);
        listView.setAdapter(adapter);

    }

}