package com.app.recommendify4.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.recommendify4.Dialogs.DialogLoading;
import com.app.recommendify4.R;
import com.app.recommendify4.RecomThreads.ArtistCallback;
import com.app.recommendify4.RecomThreads.ArtistThread;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.ThreadManagers.RecomThreadPool;
import com.app.recommendify4.UserInfo.Credentials;
import com.app.recommendify4.UserInfo.Recommendations;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class FragmentArtistRecommendation extends Fragment {

    private Recommendations userRecommendations = new Recommendations();
    private ArrayList<RecommendedArtist> artistsShown;
    private Credentials credentials;
    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
    private FragmentTransaction fragmentTransaction;

    private static final String ARTISTSHOWN = "artistShown";
    private static final String CREDENTIALS = "credentials";

    private final ArtistCallback artistThreadCallback = new ArtistCallback() {
        @Override
        public void onComplete(ArrayList<RecommendedArtist> artistRecommended) {
            userRecommendations.addArtistRecommendations(artistRecommended);
        }
    };

    public FragmentArtistRecommendation() {
    }

    public static FragmentArtistRecommendation newInstance(ArrayList<RecommendedArtist> artistShown, Credentials credentials) {
        FragmentArtistRecommendation fragment = new FragmentArtistRecommendation();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARTISTSHOWN,artistShown);
        args.putParcelable(CREDENTIALS,credentials);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistsShown = getArguments().getParcelableArrayList(ARTISTSHOWN);
            credentials = getArguments().getParcelable(CREDENTIALS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonContinue = view.findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(v -> getArtist(view));
    }

    private void getArtist(View view){
        String artist1 = ((TextView) view.findViewById(R.id.artist1)).getText().toString();
        String artist2 = ((TextView) view.findViewById(R.id.artist2)).getText().toString();
        String artist3 = ((TextView) view.findViewById(R.id.artist3)).getText().toString();

        threadPoolExecutor.execute(new ArtistThread(artist1,artist2,artist3, artistThreadCallback,credentials));

        DialogLoading dialogLoading = new DialogLoading();
        dialogLoading.show(getParentFragmentManager(),"loading");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(userRecommendations.getArtistRecommendations() != null && userRecommendations.getArtistRecommendations().size() > 0){
                        showArtists(view);
                        dialogLoading.dismiss();
                        break;
                    }
                }
            }
        },20000);

    }


    public void showArtists(View view){
        Bundle args = new Bundle();
        args.putParcelableArrayList("artistsShown",artistsShown);
        args.putParcelableArrayList("artists",userRecommendations.getArtistRecommendations());

        NavController navigation = Navigation.findNavController(view);
        navigation.navigate(R.id.fragment3Artists,args);
    }

}