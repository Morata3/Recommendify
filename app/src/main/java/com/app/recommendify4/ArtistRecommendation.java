package com.app.recommendify4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import com.app.recommendify4.Dialogs.DialogLoading;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.recommendify4.R;

public class ArtistRecommendation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_recommendation);

        Button buttonContinue =findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(v -> getArtist());

    }
    public void getArtist(){
        DialogLoading dialogLoading = new DialogLoading(ArtistRecommendation.this);
        dialogLoading.startLoadingAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                String artist1 = ((TextView) findViewById(R.id.artist1)).getText().toString();
                String artist2 = ((TextView) findViewById(R.id.artist2)).getText().toString();
                String artist3 = ((TextView) findViewById(R.id.artist3)).getText().toString();

                TextView artistRecommend = (TextView) findViewById(R.id.artistRecommend);

                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(ArtistRecommendation.this));
                }
                Python py= Python.getInstance();
                PyObject pyf = py.getModule("3ArtistRecommender");
                PyObject obj= pyf.callAttr("recommend_artist", artist1, artist2, artist3);

                artistRecommend.setText(obj.toString());
                dialogLoading.dismiss();
            }
        },3000);

    }
}