package com.app.recommendify4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recommendify4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        //Initializa and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.advanced);
        text = (TextView) findViewById(R.id.textView6);
        live = (Switch) findViewById(R.id.live);
        danceable = (Switch) findViewById(R.id.danceable);
        positive = (Switch) findViewById(R.id.positive);
        negative = (Switch) findViewById(R.id.negative);
        low = (Switch) findViewById(R.id.low);
        high = (Switch) findViewById(R.id.high);
        instru = (Switch) findViewById(R.id.instru);



        songs = (Button) findViewById(R.id.songs);
        songs.setOnClickListener(v -> FilteredSongs());


        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.advanced:

                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext()
                                , History.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;

            }
        });
    }

    private void FilteredSongs() {

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py= Python.getInstance();
        PyObject pyf = py.getModule("advanced");
        //System.out.println("Live: " + live.isChecked() + " danceable: " + danceable.isChecked() + " positive: " + positive.isChecked() + " low: " + low.isChecked() + "high: " + high.isChecked() + " instru: " + instru.isChecked());
        PyObject obj= pyf.callAttr("filter_songs",live.isChecked(),danceable.isChecked(),positive.isChecked(),negative.isChecked(),low.isChecked(),high.isChecked(),instru.isChecked());
        text.setText(obj.toString());



    }
}
