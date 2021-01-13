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
import com.app.recommendify4.UserInfo.Recommendations;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private static final String CREDENTIALS = "credentials";
    private static final String RECOMMENDATIONS = "recommendations";

    private FragmentLauncher_History fragmentLauncher;
    private Recommendations recommendations;
    private Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recommendations = getRecommendations();
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
                        intent.putExtra(CREDENTIALS, credentials);
                        intent.putExtra(RECOMMENDATIONS,recommendations);
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

    private Recommendations getRecommendations(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey(RECOMMENDATIONS))
            return parameters.getParcelable(RECOMMENDATIONS);
        else
            return null;
    }

    private Credentials getCredentials(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey(CREDENTIALS))
            return (Credentials) parameters.get(CREDENTIALS);
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
        fragmentLauncher = FragmentLauncher_History.newInstance(recommendations);
        fragmentTransaction.replace(R.id.fragmentHistory,fragmentLauncher).commit();
    }

}