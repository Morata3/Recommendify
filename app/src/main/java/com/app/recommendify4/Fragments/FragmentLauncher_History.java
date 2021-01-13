package com.app.recommendify4.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyItems.Artist.Artist;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.Song;
import com.app.recommendify4.UserInfo.Recommendations;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class FragmentLauncher_History extends Fragment {

    private static final String RECOMMENDATIONS = "recommendations";

    private Recommendations recommendations;
    private ArrayList<RecommendedSong> songs = new ArrayList<>();
    private ArrayList<RecommendedSong> hybridSongs = new ArrayList<>();
    private ArrayList<RecommendedArtist> artists = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPaperAdapter viewAdapter;
    private TabLayoutMediator tabMediator;

    private FragmentHistorySong fragmentHistorySong;
    private FragmentHistorySong fragmentHistoryHybrid;
    private FragmentHistoryArtist fragmentHistoryArtist;

    public FragmentLauncher_History() {
        // Required empty public constructor
    }

    public static FragmentLauncher_History newInstance(Recommendations recommendations) {
        FragmentLauncher_History fragment = new FragmentLauncher_History();
        Bundle args = new Bundle();
        args.putParcelable(RECOMMENDATIONS,recommendations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recommendations = getArguments().getParcelable(RECOMMENDATIONS);
            songs = recommendations.getSongsRecommendations();
            artists = recommendations.getArtistsShown();
            hybridSongs = recommendations.getHybridRecommendations();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_launcher_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_history);
        viewPager = (ViewPager2) view.findViewById(R.id.view_paper_hisotry);
        viewAdapter = new ViewPaperAdapter(this);

        fragmentHistorySong = FragmentHistorySong.newInstance(songs);
        fragmentHistoryHybrid = FragmentHistorySong.newInstance(hybridSongs);
        fragmentHistoryArtist = FragmentHistoryArtist.newInstance(artists);

        viewAdapter.addFragment(fragmentHistorySong,"Songs");
        viewAdapter.addFragment(fragmentHistoryArtist,"Artists");
        viewAdapter.addFragment(fragmentHistoryHybrid, "Hybrid");
        viewPager.setAdapter(viewAdapter);

        tabMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: {
                        tab.setText("Songs");
                        tab.setIcon(R.drawable.ic_song);
                        break;
                    }
                    case 1: {
                        tab.setText("Artists");
                        tab.setIcon(R.drawable.ic_artist);
                        break;
                    }
                    case 2: {
                        tab.setText("Hybrid");
                        tab.setIcon(R.drawable.ic_hybrid);
                        break;
                    }
                }
            }
        });
        tabMediator.attach();
    }
}