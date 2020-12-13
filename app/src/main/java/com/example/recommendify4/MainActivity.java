package com.example.recommendify4;
import com.example.recommendify4.UserInfo.Artist;
import com.example.recommendify4.UserInfo.Song;
import com.example.recommendify4.UserInfo.UserProfile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.recommendify4.UserInfo.UserProfileBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_RESULT = "result";
    public static final String CANCIONES = "Canciones";

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "4b1b0a636b8046a7b305efbf5745c09b";
    private static final String REDIRECT_URI = "recommendify://";
    private FloatingActionButton infoButton;
    private Button artistButton;

    public TextView text;
    public String myresult;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        userProfile = new UserProfile(userPreferences.getString("UserToken",null));
        UserProfileBuilder builder = new UserProfileBuilder();
        builder.execute(userProfile);

        ArrayList<Song> TopSongs = userProfile.getTopSongs();
       // System.out.println("Top songs:\n" + TopSongs);
        List<Song> TopSongsAux  = userProfile.getTopSongs().subList(0,10);


       // for(Song song: userProfile.getRecentlyPlayedSongs()) System.out.println(song);

        infoButton = (FloatingActionButton) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> openDialogInfo());

        artistButton = (Button) findViewById(R.id.buttonArtist);
        artistButton.setOnClickListener(v -> artistRecommendation());


        //Initializa and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
          Data myData = new Data.Builder()
                .putString(CANCIONES, TopSongsAux.toString())
                .build();
        OneTimeWorkRequest recommendWork = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(myData)
                .build();
        WorkManager.getInstance().enqueue(recommendWork);

        WorkManager.getInstance().getWorkInfoByIdLiveData(recommendWork.getId())
                .observe(this, info -> {
                    if (info != null && info.getState().isFinished()) {
                        myresult = info.getOutputData().getString(KEY_RESULT);
                        System.out.println("Recomendaciones: " + myresult);


                    }
                });




        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
              switch (menuItem.getItemId()) {//        setContentView(R.layout.activity_main);

                case R.id.advanced:
                    startActivity(new Intent(getApplicationContext()
                            , Advanced.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.home:
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void openDialogInfo(){
        DialogInfo dialogInfo = new DialogInfo();
        dialogInfo.show(getSupportFragmentManager(),"Dialog Information");
    }

    public void artistRecommendation(){
        Intent intent = new Intent(this, ArtistRecommendation.class);
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
    }


}
