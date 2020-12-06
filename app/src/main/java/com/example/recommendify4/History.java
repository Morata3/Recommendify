package com.example.recommendify4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Initializa and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.history);

        TextView text = (TextView) findViewById(R.id.textView5);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py= Python.getInstance();
        PyObject pyf = py.getModule("FinalRecomendator");
        PyObject obj= pyf.callAttr("rank_song_similarity_by_measure","Bohemian Rhapsody","Queen",2);
        text.setText(obj.toString());


        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.advanced:
                        startActivity(new Intent(getApplicationContext()
                                , Advanced.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        return true;
                }
                return false;

            }
        });


    }
}