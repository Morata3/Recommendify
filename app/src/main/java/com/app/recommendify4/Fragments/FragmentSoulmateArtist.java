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
import com.app.recommendify4.UserInfo.Recommendations;
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

    private static final String RECOMMENDATIONS = "Recommendations";
    private static final String ARTISTSSHOWNLISTKEY = "ArtistsShown";
    private static final String USERARTISTSLISTKEY = "UserArtists";
    private static final String LASTINDEXPROCESSED = "LastIndexProcessed";
    private static final String CREDENTIALS = "Credentials";

    private Recommendations recommendations;
    private ArrayList<RecommendedArtist> listOfRecommendations;
    private ArrayList<UserArtist> userArtists;
    private Credentials credentials;

    private TextView artistNameView;
    private ImageView artistImage;

    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
    private final CollaborativeCallback collaborativeThreadCallback = new CollaborativeCallback() {
        @Override
        public synchronized void onComplete(ArrayList<RecommendedArtist> recommendations) {
            listOfRecommendations.addAll(recommendations);
            if(listOfRecommendations.size() == 0){
                int lastIndexUsed = getLastIndexUsed(userArtists);
                UserArtist artist = userArtists.get(lastIndexUsed);
                artist.setUsed(1);
                threadPoolExecutor.execute(new CollaborativeThread(artist, collaborativeThreadCallback, credentials));
            }
        }
    };


    public FragmentSoulmateArtist() {
        // Required empty public constructor
    }

    public static FragmentSoulmateArtist newInstance(Recommendations recommendations, ArrayList<UserArtist> userArtists, Credentials credentials) {
        FragmentSoulmateArtist fragment = new FragmentSoulmateArtist();
        Bundle args = new Bundle();
        args.putParcelable(RECOMMENDATIONS, recommendations);
        args.putParcelableArrayList(USERARTISTSLISTKEY, userArtists);
        args.putParcelable(CREDENTIALS,credentials);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recommendations = getArguments().getParcelable(RECOMMENDATIONS);
            credentials = getArguments().getParcelable(CREDENTIALS);
            userArtists = getArguments().getParcelableArrayList(USERARTISTSLISTKEY);
        }
        listOfRecommendations = recommendations.getArtistRecommendations();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            recommendations.moveToHistory(artist);

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
        int lastIndexUsed = getLastIndexUsed(userArtists);
        if(lastIndexUsed != userArtists.size()){
            int indexToProccessLast = getLastIndexToProccess(lastIndexUsed, userArtists.size());
            for (UserArtist artist : userArtists.subList(lastIndexUsed, indexToProccessLast)) {
                artist.setUsed(1);
                threadPoolExecutor.execute(new CollaborativeThread(artist, collaborativeThreadCallback, credentials));
            }
        }
        else {
            updateUserProfile();
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

    private int getLastIndexUsed(ArrayList<UserArtist> userArtists){
        for(UserArtist artist: userArtists) if(!artist.isUsed()) return userArtists.indexOf(artist);
        return 0;
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

    /*private synchronized void addToArtistIndex(int toAdd){
        this.lastIndexProcessed += toAdd;
    }*/

}