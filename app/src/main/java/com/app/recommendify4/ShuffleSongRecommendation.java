package com.app.recommendify4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.Song;

public class ShuffleSongRecommendation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shufflesong_recommendation);

        TextView songRecommendedView = (TextView) findViewById(R.id.songRecommended);
        RecommendedSong recommendation = getRecommendation();

        if(recommendation != null) songRecommendedView.setText(recommendation.toString());
        else songRecommendedView.setText("No recommendations yet.");
    }

    private RecommendedSong getRecommendation(){
        Intent intent = getIntent();
        Bundle parameters = intent.getExtras();
        if(parameters != null && parameters.containsKey("songToRecommend")) return (RecommendedSong)parameters.get("songToRecommend");
        else return null;
    }

}
