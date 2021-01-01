package com.app.recommendify4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import com.app.recommendify4.Dialogs.DialogLoading;
import com.app.recommendify4.RecomThreads.ArtistCallback;
import com.app.recommendify4.RecomThreads.ArtistThread;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;
import com.app.recommendify4.ThreadManagers.RecomThreadPool;
import com.app.recommendify4.UserInfo.Recommendations;


import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class ArtistRecommendation extends AppCompatActivity {

    private Recommendations userRecommendations = new Recommendations();
    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
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

    }
    public void getArtist(){
        String artist1 = ((TextView) findViewById(R.id.artist1)).getText().toString();
        String artist2 = ((TextView) findViewById(R.id.artist2)).getText().toString();
        String artist3 = ((TextView) findViewById(R.id.artist3)).getText().toString();

        threadPoolExecutor.execute(new ArtistThread(artist1,artist2,artist3, artistThreadCallback));

        DialogLoading dialogLoading = new DialogLoading(ArtistRecommendation.this);
        dialogLoading.startLoadingAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(userRecommendations.getArtistRecommendations() != null && userRecommendations.getArtistRecommendations().size() > 0){
                        setArtist(userRecommendations.getArtistRecommendations());
                        dialogLoading.dismiss();
                        break;
                    }
                }
            }
        },10000);

    }

    //PROVISIONAL, CAMBIARASE POLO FRAGMENT DE ARTIST DE CAMILO
    public void setArtist(ArrayList<RecommendedArtist> artistRecommended){
        String artist;
        TextView artistRecommend = (TextView) findViewById(R.id.artistRecommend);
        for (int index = 0; index < artistRecommended.size(); index++) {
            artistRecommend.setText(artistRecommended.get(index).getName());
        }
    }

}