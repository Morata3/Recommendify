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



public class FragmentSong extends Fragment {

    private static final String SONGRECOMMENDED = "RecommendedSong";
    private static final String CREDENTIALS = "Credentials";
    private ArrayList<Song> listOfRecommendations;

    private Song song;
    private Credentials credentials;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private TextView songNameView;
    private TextView songArtistView;
    private ImageView coverAlbum;


    public FragmentSong() {
        // Required empty public constructor
    }

    public static FragmentSong newInstance(ArrayList<Song> songsToRecommend, Credentials credentials) {
        FragmentSong fragment = new FragmentSong();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SONGRECOMMENDED, songsToRecommend);
        args.putParcelable(CREDENTIALS,credentials);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listOfRecommendations = getArguments().getParcelableArrayList(SONGRECOMMENDED);
            credentials = getArguments().getParcelable(CREDENTIALS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();
        song = listOfRecommendations.get(0);
        listOfRecommendations.remove(song);

        ThreadLauncher builder_updateTrack = new ThreadLauncher();
        builder_updateTrack.execute(new Runnable() {
            @Override
            public void run() {
                String response = RequestSender.getTrackInfo(credentials,song.getId());
                ResponseProcessor.processTrackResponse(response,song);
            }
        });

        System.out.println("IMAGEN: " + song.getCoverURL());

        if(song != null){
            songNameView.setText(song.getName());
            songArtistView.setText(song.getArtists().get(0).getName());
            Glide.with(this).load(song.getCoverURL()).into(coverAlbum);
            try {
                if(song.getPrewviewURL() != null) playSOng();
                else System.out.println("Song without preview URL");
            } catch (IOException e) {
                System.out.println("Error trying to play song");
                e.printStackTrace();
            }
        }
        else{
            songNameView.setText("No more songs");
        }
    }

    public void playSOng() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(song.getPrewviewURL());
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

}