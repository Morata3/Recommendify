package com.app.recommendify4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import com.app.recommendify4.Dialogs.DialogLoading;
import com.app.recommendify4.Fragments.Fragment3Artists;
import com.app.recommendify4.Fragments.FragmentLauncher;
import com.app.recommendify4.RecomThreads.ArtistCallback;
import com.app.recommendify4.RecomThreads.ArtistThread;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.ThreadManagers.RecomThreadPool;
import com.app.recommendify4.UserInfo.Credentials;
import com.app.recommendify4.UserInfo.Recommendations;


import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class ArtistRecommendation extends AppCompatActivity {

    private Recommendations userRecommendations = new Recommendations();
    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
    private FragmentLauncher fragmentLauncher;
    private Fragment3Artists fragment3Artists;
    private FragmentTransaction fragmentTransaction;

    private final ArtistCallback artistThreadCallback = new ArtistCallback() {
        @Override
        public void onComplete(ArrayList<RecommendedArtist> artistRecommended) {
            userRecommendations.addArtistRecommendations(artistRecommended);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_recommendation);

        Button buttonContinue =findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(v -> getArtist());
        setupFragment();
    }
    public void getArtist(){
        String artist1 = ((TextView) findViewById(R.id.artist1)).getText().toString();
        String artist2 = ((TextView) findViewById(R.id.artist2)).getText().toString();
        String artist3 = ((TextView) findViewById(R.id.artist3)).getText().toString();

        System.out.println("CREDENTIALS: " + getCredentials().getTime_to_expire());

        threadPoolExecutor.execute(new ArtistThread(artist1,artist2,artist3, artistThreadCallback,getCredentials()));


        DialogLoading dialogLoading = new DialogLoading(ArtistRecommendation.this);
        dialogLoading.startLoadingAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(userRecommendations.getArtistRecommendations() != null && userRecommendations.getArtistRecommendations().size() > 0){
                        dialogLoading.dismiss();
                        setArtistView(userRecommendations.getArtistRecommendations());
                        break;
                    }
                }
            }
        },20000);
    }

    public void setupFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentLauncher = new FragmentLauncher();
        fragment3Artists = new Fragment3Artists();

        fragmentTransaction.add(R.id.fragmentMain3Artist,fragmentLauncher).commit();
    }

    public void setArtistView(ArrayList<RecommendedArtist> artistRecommended){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment3Artists = Fragment3Artists.newInstance(artistRecommended,getArtistShown());
        fragmentTransaction.replace(R.id.fragmentMain3Artist,fragment3Artists);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    private Credentials getCredentials(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("Credentials")){
            return parameters.getParcelable("Credentials");}
        else
            return null;
    }

    private ArrayList<RecommendedArtist> getArtistShown(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("artistShown")){
            return parameters.getParcelableArrayList("artistShown");
        }
        else
            return null;
    }

}