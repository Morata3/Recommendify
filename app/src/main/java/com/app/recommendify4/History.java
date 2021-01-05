package com.app.recommendify4;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.app.recommendify4.Fragments.FragmentLauncher_History;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.UserInfo.Credentials;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private FragmentLauncher_History fragmentLauncher;
    private ArrayList<RecommendedArtist> artists;
    private ArrayList<RecommendedSong> songs;
    private Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        artists = getArtists();
        songs = getSongs();
        credentials = getCredentials();
        setupFragment();
        changeFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.advanced:
                        intent = new Intent(getApplicationContext(), Advanced.class);
                        intent.putExtra("credentials", credentials);
                        intent.putParcelableArrayListExtra("Songs",songs);
                        intent.putParcelableArrayListExtra("Artists",artists);
                        startActivity(intent);

                    case R.id.home:
                        finish();
                    case R.id.history:
                        return true;
                }
                return false;

            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    private ArrayList<RecommendedSong> getSongs(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("Songs"))
            return parameters.getParcelableArrayList("Songs");
        else
            return null;
    }

    private ArrayList<RecommendedArtist> getArtists(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("Artists"))
            return parameters.getParcelableArrayList("Artists");
        else
            return null;
    }

    private Credentials getCredentials(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("credentials"))
            return (Credentials) parameters.get("credentials");
        else
            return null;
    }


    public void setupFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentLauncher = new FragmentLauncher_History();

        fragmentTransaction.add(R.id.fragmentHistory,fragmentLauncher).commit();
    }

    public void changeFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentLauncher = FragmentLauncher_History.newInstance(songs, artists);
        fragmentTransaction.replace(R.id.fragmentHistory,fragmentLauncher).commit();
    }

}