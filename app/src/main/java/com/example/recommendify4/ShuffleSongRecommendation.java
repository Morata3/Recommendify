package com.example.recommendify4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recommendify4.SpotifyItems.Song;

import java.util.ArrayList;

public class ShuffleSongRecommendation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shufflesong_recommendation);

        TextView songRecommendedView = (TextView) findViewById(R.id.songRecommended);
        Song recommendation = getRecommendationsList();

        if(recommendation != null) songRecommendedView.setText(recommendation.toString());
        else songRecommendedView.setText("No recommendations yet.");
    }

    private Song getRecommendationsList(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("songToRecommend")) return (Song)parameters.get("songToRecommend");
        else return null;
    }

}
