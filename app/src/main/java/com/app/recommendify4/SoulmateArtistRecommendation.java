package com.app.recommendify4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.app.recommendify4.SpotifyItems.Artist.Artist;
import com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist;

public class SoulmateArtistRecommendation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soulmate_artist_racommendation);

        TextView artistRecommendedView = (TextView) findViewById(R.id.artistRecommend);
        RecommendedArtist recommendation = getRecommendation();

        if(recommendation != null)
            artistRecommendedView.setText(recommendation.toString());
        else
            artistRecommendedView.setText("Building artist recommendations.");
    }

    private RecommendedArtist getRecommendation(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("artistToRecommend"))
            return (RecommendedArtist)parameters.get("artistToRecommend");
        else
            return null;
    }

}