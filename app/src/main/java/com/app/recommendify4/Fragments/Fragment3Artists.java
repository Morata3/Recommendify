package com.app.recommendify4.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class Fragment3Artists extends Fragment {

    private ArrayList<RecommendedArtist> recommendedArtists;
    private ArrayList<RecommendedArtist> artistShown;

    private static final String ARTISTS = "artists";
    private static final String ARTISTSSHOWN = "artistsShown";


    public Fragment3Artists() {
        // Required empty public constructor
    }


    public static Fragment3Artists newInstance(ArrayList<RecommendedArtist> artists, ArrayList<RecommendedArtist> artistsShown) {
        Fragment3Artists fragment = new Fragment3Artists();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTISTS,artists);
        args.putParcelableArrayList(ARTISTSSHOWN,artistsShown);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recommendedArtists = getArguments().getParcelableArrayList(ARTISTS);
            artistShown = getArguments().getParcelableArrayList(ARTISTSSHOWN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_3artists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] covers = new String[recommendedArtists.size()];
        String[] names = new String[recommendedArtists.size()];

        for(int index=0; index<recommendedArtists.size(); index++){
            RecommendedArtist artist = recommendedArtists.get(index);
            covers[index] = artist.getImage();
            names[index] = artist.getName();
            artistShown.add(artist);
        }

        ImageView cover1 = (ImageView) view.findViewById(R.id.coverArtist1);
        if(covers[0] != null) Glide.with(this).load(covers[0]).into(cover1);
        ImageView cover2 = (ImageView) view.findViewById(R.id.coverArtist2);
        if(covers[1] != null) Glide.with(this).load(covers[1]).into(cover2);
        ImageView cover3 = (ImageView) view.findViewById(R.id.coverArtist3);
        if(covers[2] != null) Glide.with(this).load(covers[2]).into(cover3);
        ImageView cover4 = (ImageView) view.findViewById(R.id.coverArtist4);
        if(covers[3] != null) Glide.with(this).load(covers[3]).into(cover4);
        ImageView cover5 = (ImageView) view.findViewById(R.id.coverArtist5);
        if(covers[4] != null) Glide.with(this).load(covers[4]).into(cover5);
        ImageView cover6 = (ImageView) view.findViewById(R.id.coverArtist6);
        if(covers[5] != null) Glide.with(this).load(covers[5]).into(cover6);

        TextView artist1 = (TextView) view.findViewById(R.id.artist1);
        if(names[0] != null) artist1.setText(names[0]);
        TextView artist2 = (TextView) view.findViewById(R.id.artist2);
        if(names[1] != null) artist2.setText(names[1]);
        TextView artist3 = (TextView) view.findViewById(R.id.artist3);
        if(names[2] != null) artist3.setText(names[2]);
        TextView artist4 = (TextView) view.findViewById(R.id.artist4);
        if(names[3] != null) artist4.setText(names[3]);
        TextView artist5 = (TextView) view.findViewById(R.id.artist5);
        if(names[4] != null) artist5.setText(names[4]);
        TextView artist6 = (TextView) view.findViewById(R.id.artist6);
        if(names[5] != null) artist6.setText(names[5]);

        Button tryAgain = (Button) view.findViewById(R.id.tryAgainButton);
        tryAgain.setOnClickListener(v ->{
            NavController navigation = Navigation.findNavController(view);
            navigation.navigate(R.id.fragmentLauncher);
        });

    }

}