package com.example.recommendify4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class ArtistRecommendation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_recommendation);

        Button buttonContinue =findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(v -> getArtist());


    }
    public void getArtist(){
        final LoadingDialog loadingDialog = new LoadingDialog(ArtistRecommendation.this);
        loadingDialog.startLoadingAnimation();

        String artist1 = ((TextView) findViewById(R.id.artist1)).getText().toString();
        String artist2 = ((TextView) findViewById(R.id.artist2)).getText().toString();
        String artist3 = ((TextView) findViewById(R.id.artist3)).getText().toString();

        TextView artistRecommend = (TextView) findViewById(R.id.artistRecommend);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py= Python.getInstance();
        PyObject pyf = py.getModule("3ArtistRecommender");
        PyObject obj= pyf.callAttr("recommend_artist", artist1, artist2, artist3);

        loadingDialog.dismiss();
        artistRecommend.setText(obj.toString());
    }
}