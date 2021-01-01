package com.app.recommendify4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.recommendify4.Fragments.FragmentHybrid;
import com.app.recommendify4.Fragments.FragmentLauncher;
import com.app.recommendify4.Fragments.FragmentAdvanced;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.UserInfo.Credentials;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class Advanced extends AppCompatActivity {
    private Button songs;
    private TextView text;
    private Switch live;
    private Switch high;
    private Switch low;
    private Switch instru;
    private Switch positive;
    private Switch negative;
    private Switch danceable;
    private Credentials credentials;

    private FragmentTransaction fragmentTransaction;
    private Fragment fragmentLauncher;
    private FragmentAdvanced fragmentAdvanced;
    ArrayList<RecommendedSong> recommendationsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);
        setupFragment();
        //Initializa and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.advanced);
        text = (TextView) findViewById(R.id.textView6);
        live = (Switch) findViewById(R.id.live);
        danceable = (Switch) findViewById(R.id.danceable);
        positive = (Switch) findViewById(R.id.positive);
        negative = (Switch) findViewById(R.id.negative);
        low = (Switch) findViewById(R.id.low);
        high = (Switch) findViewById(R.id.high);
        instru = (Switch) findViewById(R.id.instru);


        credentials = getCredentials();

        songs = (Button) findViewById(R.id.songs);
        songs.setOnClickListener(v -> FilteredSongs());

        low.setOnClickListener(v -> ChangeLowSwitch());
        high.setOnClickListener(v -> ChangeHighSwitch());
        positive.setOnClickListener(v -> ChangePositiveSwitch());
        negative.setOnClickListener(v -> ChangeNegativeSwitch());

        //Perform ItemSelectedListener
        System.out.println("o por aqui?");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.advanced:

                        return true;
                    case R.id.home:

                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:

                        startActivity(new Intent(getApplicationContext()
                                , History.class));
                        overridePendingTransition(0, 0);
                        return true;

                }

                return false;

            }
        });
    }

    public void onClick(View view){
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.songs:

                if(1==1){
                    fragmentAdvanced = FragmentAdvanced.newInstance(FilteredSongs(),credentials);
                    fragmentTransaction.replace(R.id.fragmentMain,fragmentAdvanced);
                    fragmentTransaction.addToBackStack(null);
                }else System.out.println("Recommendations not yet ready ");
                break;

            }
        }

    private ArrayList<RecommendedSong> FilteredSongs() {

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("advanced");
        //System.out.println("Live: " + live.isChecked() + " danceable: " + danceable.isChecked() + " positive: " + positive.isChecked() + " low: " + low.isChecked() + "high: " + high.isChecked() + " instru: " + instru.isChecked());
        PyObject obj= pyf.callAttr("filter_songs",live.isChecked(),danceable.isChecked(),positive.isChecked(),negative.isChecked(),low.isChecked(),high.isChecked(),instru.isChecked());
        String recommendations = obj.toString();

        recommendations = recommendations.substring(1,recommendations.length() - 1);
        String[] Recoms = recommendations.split("],");

        for(int k = 1; k < Recoms.length - 1; k++){
            String title = Recoms[k].split(",")[0].substring(3,Recoms[k].split(",")[0].length()-1);
            String id =Recoms[k].split(",")[1].substring(2,Recoms[k].split(",")[1].length()-1);
            String artist = Recoms[k].split(",")[2].substring(2,Recoms[k].split(",")[2].length()-1);
            RecommendedSong recommendedSong = new RecommendedSong(title, artist, id, 0);
            recommendationsList.add(recommendedSong);
        }

        return recommendationsList;

    }

    public void setupFragment(){
        fragmentLauncher = new FragmentLauncher();
        fragmentAdvanced = new FragmentAdvanced();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentMain,fragmentLauncher).commit();

    }

    private void ChangePositiveSwitch() {

        if (negative.isChecked()) {

            negative.toggle();

        }
    }

    private void ChangeNegativeSwitch() {

        if (positive.isChecked()) {

            positive.toggle();

        }
    }

    private void ChangeLowSwitch() {

        if (high.isChecked()) {

            high.toggle();

        }
    }

    private void ChangeHighSwitch() {

        if (low.isChecked()) {

            low.toggle();

        }
    }

    private Credentials getCredentials(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        System.out.println("Byndle: " + parameters);
        if(parameters != null && parameters.containsKey("credentials"))
            return (Credentials) parameters.get("credentials");
        else
            return null;
    }

    }

