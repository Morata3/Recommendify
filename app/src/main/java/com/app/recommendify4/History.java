package com.app.recommendify4;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import com.app.recommendify4.Fragments.FragmentHistoryArtist;
import com.app.recommendify4.Fragments.FragmentHistorySong;
import com.app.recommendify4.Fragments.FragmentLauncher;
import com.app.recommendify4.Fragments.FragmentLauncher_History;
import com.app.recommendify4.Fragments.FragmentSong;
import com.app.recommendify4.Fragments.FragmentSoulmateArtist;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.UserInfo.Credentials;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private FragmentLauncher_History fragmentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupFragment();
        changeFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.advanced:
                        startActivity(new Intent(getApplicationContext()
                                , Advanced.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        /*startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);*/
                        finish();
                        return true;
                    case R.id.history:
                        return true;
                }
                return false;

            }
        });
    }

    private ArrayList<RecommendedSong> getSongs(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("Songs")){
            return parameters.getParcelableArrayList("Songs");}
        else
            return null;
    }

    private ArrayList<RecommendedArtist> getArtists(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("Artists")){
            return parameters.getParcelableArrayList("Artists");}
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
        fragmentLauncher = FragmentLauncher_History.newInstance(getSongs(), getArtists());
        fragmentTransaction.replace(R.id.fragmentHistory,fragmentLauncher).commit();
    }

}