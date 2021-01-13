package com.app.recommendify4.Fragments;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.recommendify4.Login;
import com.app.recommendify4.RecomThreads.ContentCallback;
import com.app.recommendify4.RecomThreads.ContentThread;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.UserSong;
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

public class FragmentSong extends Fragment {

    private static final String RECOMMENDATIONSLISTKEY = "RecommendationsList";
    private static final String SONGSSHOWNLISTKEY = "SongsShown";
    private static final String USERSONGSLISTKEY = "UserSongs";
    private static final String LASTINDEXPROCESSED = "LastIndexProcessed";
    private static final String CREDENTIALS = "Credentials";
    private static final String GANDALFMEME = "https://memegenerator.net/img/instances/74848295/please-wait-while-im-doing-my-magic.jpg";

    private ArrayList<RecommendedSong> listOfRecommendations;
    private ArrayList<RecommendedSong> songsShown;
    private ArrayList<UserSong> userSongs;
    private Credentials credentials;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private TextView songNameView;
    private TextView songArtistView;
    private ImageView coverAlbum;

    //private int lastIndexProcessed;


    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
    private final ContentCallback contentThreadCallback = new ContentCallback() {
        @Override
        public synchronized void onComplete(ArrayList<RecommendedSong> recommendations) {
            listOfRecommendations.addAll(recommendations);
            if(listOfRecommendations.size() == 0){
                int lastIndexUsed = getLastIndexUsed(userSongs);
                UserSong song = userSongs.get(lastIndexUsed);
                song.setUsed(true);
                //addToSongIndex(1);
                threadPoolExecutor.execute(new ContentThread(song, contentThreadCallback, credentials));
            }
        }
    };

    /*public interface FragmentSongCallback{
        void updateLastSongProccessed(int lastIndexProcessed);
    }*/


    public FragmentSong() {
        // Required empty public constructor
    }

    public static FragmentSong newInstance(ArrayList<RecommendedSong> songsToRecommend, ArrayList<RecommendedSong> songsShown, ArrayList<UserSong> userSongs, Credentials credentials/*, int lastIndexProcessed*/) {
        FragmentSong fragment = new FragmentSong();
        Bundle args = new Bundle();
        args.putParcelableArrayList(RECOMMENDATIONSLISTKEY, songsToRecommend);
        args.putParcelableArrayList(SONGSSHOWNLISTKEY, songsShown);
        args.putParcelableArrayList(USERSONGSLISTKEY, userSongs);
        args.putParcelable(CREDENTIALS,credentials);
        //args.putInt(LASTINDEXPROCESSED, lastIndexProcessed);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listOfRecommendations = getArguments().getParcelableArrayList(RECOMMENDATIONSLISTKEY);
            songsShown = getArguments().getParcelableArrayList(SONGSSHOWNLISTKEY);
            userSongs = getArguments().getParcelableArrayList(USERSONGSLISTKEY);
            credentials = getArguments().getParcelable(CREDENTIALS);
            //lastIndexProcessed = getArguments().getInt(LASTINDEXPROCESSED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton nextSong = (ImageButton) view.findViewById(R.id.nextSong);
        nextSong.setOnClickListener(v -> setNextSong());
        coverAlbum = (ImageView) view.findViewById(R.id.playSong);
        songNameView = (TextView) view.findViewById(R.id.songName);
        songArtistView = (TextView) view.findViewById(R.id.songArtist);
        setNextSong();
    }

    public void setNextSong() {
        if(listOfRecommendations.size() > 0){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            RecommendedSong song = listOfRecommendations.get(0);
            listOfRecommendations.remove(song);
            songsShown.add(song);

            songNameView.setText(song.getName());
            songArtistView.setText(song.getArtists().get(0).getName());
            Glide.with(this).load(song.getCoverURL()).into(coverAlbum);
            try {
                if(song.getPreviewURL() != null) {
                    if (!song.getPreviewURL().equals("null"))
                        playSong(song);
                }else System.out.println("Song without preview URL");
            } catch (IOException e) {
                System.out.println("Error trying to play song");
                e.printStackTrace();
            }
            if(listOfRecommendations.size() == 0) generateMoreRecommendations();

        }
        else{
            songNameView.setText("No more recommendations for now");
            songArtistView.setText("But stay calm. More are being generated");
            Glide.with(this).load(GANDALFMEME).into(coverAlbum);

          //  Glide.with(this).clear(coverAlbum);
        }

    }

    public void playSong(RecommendedSong song) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(song.getPreviewURL());
        mediaPlayer.setOnPreparedListener(this::onPrepared);
        mediaPlayer.prepareAsync();
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    private void generateMoreRecommendations(){
        int lastIndexUsed = getLastIndexUsed(userSongs);
        if(lastIndexUsed != userSongs.size()){
            int indexToProccessLast = getLastIndexToProccess(lastIndexUsed, userSongs.size());
            for (UserSong song : userSongs.subList(lastIndexUsed, indexToProccessLast)) {
                song.setUsed(true);
                threadPoolExecutor.execute(new ContentThread(song, contentThreadCallback, credentials));
                //lastIndexUsed = indexToProccessLast;
            }
        }
        else {
            updateUserProfile();
            //lastIndexProcessed = 0;
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
        userSongs = userProfile.getTopSongs();
    }


    private int getLastIndexToProccess(int currentIndex, int listSize){
        if(currentIndex + 5 <= listSize) return currentIndex + 5;
        else return listSize;
    }

    private int getLastIndexUsed(ArrayList<UserSong> songs){
        for(UserSong song : songs) if(!song.isUsed()) return songs.indexOf(song);
        return 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.release();
        if(listOfRecommendations.size() == 0) generateMoreRecommendations();

    }

    /*private synchronized void addToSongIndex(int toAdd){
        this.lastIndexProcessed += toAdd;
    }*/
}

