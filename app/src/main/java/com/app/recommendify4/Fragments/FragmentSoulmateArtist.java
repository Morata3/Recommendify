package com.app.recommendify4.Fragments;

import android.content.SharedPreferences;
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
import com.app.recommendify4.Login;
import com.app.recommendify4.RecomThreads.CollaborativeCallback;
import com.app.recommendify4.RecomThreads.CollaborativeThread;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.ThreadManagers.RecomThreadPool;
import com.app.recommendify4.UserInfo.UserProfile;
import com.bumptech.glide.Glide;
import com.app.recommendify4.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import com.app.recommendify4.ThreadManagers.ThreadLauncher;
import com.app.recommendify4.UserInfo.Credentials;
import com.google.gson.Gson;
import static android.content.Context.MODE_PRIVATE;

public class FragmentSoulmateArtist extends Fragment {

    private static final String RECOMMENDATIONSLISTKEY = "RecommendationsList";
    private static final String ARTISTSSHOWNLISTKEY = "ArtistsShown";
    private static final String USERARTISTSLISTKEY = "Userrtists";
    private static final String LASTINDEXPROCESSED = "LastIndexProcessed";
    private static final String CREDENTIALS = "Credentials";

    private ArrayList<RecommendedArtist> listOfRecommendations;
    private ArrayList<RecommendedArtist> artistsShown;
    private ArrayList<UserArtist> userArtists;
    private Credentials credentials;

    private TextView artistNameView;
    private ImageView artistImage;

    private int lastIndexProcessed; //OS PRIMEIROS 5 PROCESANSE NO ON_CREATE DO MAIN ACTIVITY


    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
    private final CollaborativeCallback collaborativeThreadCallback = new CollaborativeCallback() {
        @Override
        public synchronized void onComplete(ArrayList<RecommendedArtist> recommendations) {
            listOfRecommendations.addAll(recommendations);
        }
    };


    public FragmentSoulmateArtist() {
        // Required empty public constructor
    }

    public static FragmentSoulmateArtist newInstance(ArrayList<RecommendedArtist> artistsToRecommend, ArrayList<RecommendedArtist> artistsShown, ArrayList<UserArtist> userArtists, Credentials credentials, int lastIndexProcessed) {
        FragmentSoulmateArtist fragment = new FragmentSoulmateArtist();
        Bundle args = new Bundle();
        args.putParcelableArrayList(RECOMMENDATIONSLISTKEY, artistsToRecommend);
        args.putParcelableArrayList(ARTISTSSHOWNLISTKEY, artistsShown);
        args.putParcelableArrayList(USERARTISTSLISTKEY, userArtists);
        args.putParcelable(CREDENTIALS,credentials);
        args.putInt(LASTINDEXPROCESSED, lastIndexProcessed);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listOfRecommendations = getArguments().getParcelableArrayList(RECOMMENDATIONSLISTKEY);
            artistsShown = getArguments().getParcelableArrayList(ARTISTSSHOWNLISTKEY);
            userArtists = getArguments().getParcelableArrayList(USERARTISTSLISTKEY);
            credentials = getArguments().getParcelable(CREDENTIALS);
            lastIndexProcessed = getArguments().getInt(LASTINDEXPROCESSED);
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

        ImageButton nextSong = (ImageButton) view.findViewById(R.id.nextArtist);
        nextSong.setOnClickListener(v -> setNextArtist());
        artistImage = (ImageView) view.findViewById(R.id.artistImage);
        artistNameView = (TextView) view.findViewById(R.id.artistName);
        setNextArtist();
    }

    public void setNextArtist() {
        if(listOfRecommendations.size() > 0){
            RecommendedArtist artist = listOfRecommendations.get(0);
            listOfRecommendations.remove(artist);
            artistsShown.add(artist);

            artistNameView.setText(artist.getName());
            Glide.with(this).load(artist.getImage()).into(artistImage);

            if(listOfRecommendations.size() == 0) generateMoreRecommendations();

        }
        else{
            artistNameView.setText("No more recommendations for now");
            Glide.with(this).clear(artistImage);
        }

    }



    private void generateMoreRecommendations(){
        if(lastIndexProcessed != userArtists.size()){
            int indexToProccessLast = getLastIndexToProccess(lastIndexProcessed, userArtists.size());
            for (UserArtist artist : userArtists.subList(lastIndexProcessed, indexToProccessLast)) {
                threadPoolExecutor.execute(new CollaborativeThread(artist, collaborativeThreadCallback, credentials));
                lastIndexProcessed = indexToProccessLast;
            }
        }
        else {
            updateUserProfile();
            lastIndexProcessed = 0;
        }


    }

    private void updateUserProfile(){
        SharedPreferences login = getActivity().getSharedPreferences(Login.PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor login_editor = login.edit();
        UserProfile userProfile = new UserProfile(credentials);
        ThreadLauncher builder_profile = new ThreadLauncher();
        builder_profile.execute(userProfile);
        Gson gson = new Gson();
        String userProfile_json = gson.toJson(userProfile);
        login_editor.putString(Login.PREFERENCES_USER, userProfile_json);
        login_editor.apply();
        userArtists = userProfile.getTopArtists();
    }


    private int getLastIndexToProccess(int currentIndex, int listSize){
        if(currentIndex + 5 <= listSize) return currentIndex + 5;
        else return listSize;
    }


    @Override
    public void onStop() {
        super.onStop();
        if(listOfRecommendations.size() == 0) generateMoreRecommendations();

    }

}