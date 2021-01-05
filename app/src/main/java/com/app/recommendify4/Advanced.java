package com.app.recommendify4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.MenuItem;

import com.app.recommendify4.Fragments.FragmentHybrid;
import com.app.recommendify4.Fragments.FragmentLauncher;
import com.app.recommendify4.Fragments.FragmentAdvanced;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.recommendify4.R;
import com.app.recommendify4.SpotifyItems.Artist.Artist;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.UserSong;
import com.app.recommendify4.UserInfo.Credentials;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Switch loud;
    private Switch quiet;
    private Credentials credentials;
    private ArrayList<RecommendedSong> songsShown;
    private ArrayList<RecommendedArtist> artistsShown;

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
        live = (Switch) findViewById(R.id.live);
        danceable = (Switch) findViewById(R.id.danceable);
        positive = (Switch) findViewById(R.id.positive);
        negative = (Switch) findViewById(R.id.negative);
        low = (Switch) findViewById(R.id.low);
        high = (Switch) findViewById(R.id.high);
        instru = (Switch) findViewById(R.id.instru);
        quiet = (Switch) findViewById(R.id.quiet);
        loud = (Switch) findViewById(R.id.loud);


        credentials = getCredentials();
        songsShown = getSongs();
        artistsShown = getArtists();
        songs = (Button) findViewById(R.id.songs);

        quiet.setOnClickListener(v -> ChangeQuietSwitch());
        loud.setOnClickListener(v -> ChangeLoudSwitch());
        low.setOnClickListener(v -> ChangeLowSwitch());
        high.setOnClickListener(v -> ChangeHighSwitch());
        positive.setOnClickListener(v -> ChangePositiveSwitch());
        negative.setOnClickListener(v -> ChangeNegativeSwitch());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.advanced);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.advanced:

                        return true;
                    case R.id.home:
                        finish();
                        return true;
                    case R.id.history:

                        intent = new Intent(getApplicationContext(), History.class);
                        intent.putParcelableArrayListExtra("Songs",songsShown);
                        intent.putParcelableArrayListExtra("Artists",artistsShown);
                        intent.putExtra("credentials", credentials);
                        startActivity(intent);
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

    public void onClick(View view) throws JSONException {
        fragmentTransaction=getSupportFragmentManager().beginTransaction();

        switch (view.getId()) {

            case R.id.songs:
                    fragmentAdvanced = FragmentAdvanced.newInstance(FilteredSongs(),credentials);
                    fragmentTransaction.replace(R.id.fragmentMainAdvanced,fragmentAdvanced);
                    fragmentTransaction.addToBackStack(null);

                break;

            }

        fragmentTransaction.commit();


    }

    private ArrayList<RecommendedSong> FilteredSongs() throws JSONException {

        recommendationsList = new ArrayList<>();

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("advanced");
        //System.out.println("Live: " + live.isChecked() + " danceable: " + danceable.isChecked() + " positive: " + positive.isChecked() + " low: " + low.isChecked() + "high: " + high.isChecked() + " instru: " + instru.isChecked());

        PyObject obj= pyf.callAttr("filter_songs",live.isChecked(),danceable.isChecked(),positive.isChecked(),negative.isChecked(),low.isChecked(),
                high.isChecked(),instru.isChecked(),loud.isChecked(),quiet.isChecked());
        String recommendations = obj.toString();

        System.out.println("Switch" + recommendations);


        JSONArray jsonrecom = null;

        jsonrecom = new JSONArray(recommendations);


        for(int index = 0; index < jsonrecom.length(); index++){
            String title = jsonrecom.getJSONObject(index).getString("song_name");
            String artist = jsonrecom.getJSONObject(index).getString("artist");
            String id = jsonrecom.getJSONObject(index).getString("id");
            RecommendedSong recommendedSong = new RecommendedSong(title, artist, id, 0,"");
            recommendationsList.add(recommendedSong);

        }

        return recommendationsList;

    }

    public void setupFragment(){

        fragmentLauncher = new FragmentLauncher();
        fragmentAdvanced = new FragmentAdvanced();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragmentMainAdvanced,fragmentLauncher).commit();

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

    private void ChangeLoudSwitch() {

        if (quiet.isChecked()) {

            quiet.toggle();

        }
    }

    private void ChangeQuietSwitch() {

        if (loud.isChecked()) {

            loud.toggle();

        }
    }

    private Credentials getCredentials(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("credentials")){
            return (Credentials) parameters.get("credentials");}
        else
            return null;
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

}

