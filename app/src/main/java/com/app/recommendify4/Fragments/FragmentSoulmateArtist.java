package com.app.recommendify4.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.recommendify4.SpotifyItems.Artist;
import com.bumptech.glide.Glide;
import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyApi.ResponseProcessor;
import com.app.recommendify4.SpotifyItems.Song;

import java.io.IOException;
import java.util.ArrayList;

import com.app.recommendify4.ThreadManagers.ThreadLauncher;
import com.app.recommendify4.UserInfo.Credentials;
import com.app.recommendify4.UserInfo.UserRecommendations;



public class FragmentSoulmateArtist extends Fragment {

    private static final String ARTISTRECOMMENDED = "RecommendedSong";
    private static final String CREDENTIALS = "Credentials";
    private ArrayList<Artist> listOfRecommendations;

    private Artist artist;
    private Credentials credentials;

    private TextView artistNameView;
    private ImageView artistImage;


    public FragmentSoulmateArtist() {
        // Required empty public constructor
    }

    public static FragmentSoulmateArtist newInstance(ArrayList<Artist> artistsToRecommend, Credentials credentials) {
        FragmentSoulmateArtist fragment = new FragmentSoulmateArtist();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTISTRECOMMENDED, artistsToRecommend);
        args.putParcelable(CREDENTIALS,credentials);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listOfRecommendations = getArguments().getParcelableArrayList(ARTISTRECOMMENDED);
            credentials = getArguments().getParcelable(CREDENTIALS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_soulmate_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton nextArtist = (ImageButton) view.findViewById(R.id.nextArtist);
        nextArtist.setOnClickListener(v -> setNextSong());
        artistImage = (ImageView) view.findViewById(R.id.artistImage);
        artistNameView = (TextView) view.findViewById(R.id.artistName);
        setNextSong();
    }

    public void setNextSong() {
        artist = listOfRecommendations.get(0);

        //Necessary to PUT it in the Historic artist list first...

        listOfRecommendations.remove(artist);

        ThreadLauncher builder_updateTrack = new ThreadLauncher();
        builder_updateTrack.execute(new Runnable() {
            @Override
            public void run() {
//                String response = RequestSender.getTrackInfo(credentials, artist.getId());
//                ResponseProcessor.processTrackResponse(response, artist);
            }
        });

        if(artist != null){
            artistNameView.setText(artist.getName());
//            Glide.with(this).load(artist.getCoverURL()).into(artistImage);

        }else{
            artistNameView.setText("No more songs");
        }
    }



}