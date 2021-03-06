package com.app.recommendify4.Fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.bumptech.glide.Glide;
import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyApi.ResponseProcessor;
import java.io.IOException;
import java.util.ArrayList;
import com.app.recommendify4.ThreadManagers.ThreadLauncher;
import com.app.recommendify4.UserInfo.Credentials;



public class FragmentHybrid extends Fragment {

    private static final String SONGRECOMMENDED = "RecommendedSong";
    private static final String SONGSHOWN = "songsShown";
    private static final String CREDENTIALS = "Credentials";

    private ArrayList<RecommendedSong> listOfRecommendations;
    private ArrayList<RecommendedSong> songShown;
    private RecommendedSong song;
    private int currentSong = 0;
    private Credentials credentials;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private TextView songNameView;
    private TextView songArtistView;
    private ImageView coverAlbum;


    public FragmentHybrid() {
        // Required empty public constructor
    }

    public static FragmentHybrid newInstance(ArrayList<RecommendedSong> songsToRecommend,ArrayList<RecommendedSong> songsShown, Credentials credentials) {
        FragmentHybrid fragment = new FragmentHybrid();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SONGRECOMMENDED, songsToRecommend);
        args.putParcelable(CREDENTIALS,credentials);
        args.putParcelableArrayList(SONGSHOWN,songsShown);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listOfRecommendations = getArguments().getParcelableArrayList(SONGRECOMMENDED);
            credentials = getArguments().getParcelable(CREDENTIALS);
            songShown = getArguments().getParcelableArrayList(SONGSHOWN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hybrid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton nextSong = (ImageButton) view.findViewById(R.id.nextSongHybrid);
        nextSong.setOnClickListener(v -> setNextSong());
        coverAlbum = (ImageView) view.findViewById(R.id.playSongHybrid);
        songNameView = (TextView) view.findViewById(R.id.songNameHybrid);
        songArtistView = (TextView) view.findViewById(R.id.songArtistHybrid);
        setNextSong();
    }

    public void setNextSong() {

        if(currentSong < listOfRecommendations.size()){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            song = listOfRecommendations.get(0);
            listOfRecommendations.remove(song);
            if(!songShown.contains(song)) songShown.add(song);

            ThreadLauncher builder_updateTrack = new ThreadLauncher();
            builder_updateTrack.execute(new Runnable() {
                @Override
                public void run() {
                    String response = RequestSender.getTrackInfo(credentials,song.getId());
                    ResponseProcessor.processTrackResponse(response,song);
                }
            });

            System.out.println("IMAGEN: " + song.getCoverURL());

            songNameView.setText(song.getName());
            songArtistView.setText(song.getArtists().get(0).getName());
            Glide.with(this).load(song.getCoverURL()).into(coverAlbum);
            try {
                if(song.getPreviewURL() != null) playSong();
                else System.out.println("Song without preview URL");
            } catch (IOException e) {
                System.out.println("Error trying to play song");
                e.printStackTrace();
            }
        }
        else songNameView.setText("No more songs");

    }

    public void playSong() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(song.getPreviewURL());
        mediaPlayer.setOnPreparedListener(this::onPrepared);
        mediaPlayer.prepareAsync();
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }

}