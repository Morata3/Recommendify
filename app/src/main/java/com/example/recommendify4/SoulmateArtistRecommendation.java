package com.example.recommendify4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recommendify4.SpotifyItems.Artist;

import java.util.ArrayList;

public class SoulmateArtistRecommendation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soulmate_artist_racommendation);

        TextView songRecommendedView = (TextView) findViewById(R.id.artistRecommend);
        Artist recommendation = getRecommendationsList();

        if(recommendation != null)
            songRecommendedView.setText(recommendation.toString());
        else
            songRecommendedView.setText("Building artist recommendations.");
    }

    private Artist getRecommendationsList(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("artistToRecommend"))
            return (Artist)parameters.get("artistToRecommend");
        else
            return null;
    }

}