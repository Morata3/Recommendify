package com.example.recommendify4;

import com.example.recommendify4.SpotifyItems.Song;
import com.example.recommendify4.UserInfo.UserProfile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.recommendify4.UserInfo.UserProfileBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    public static final String KEY_CONTENT = "content";
    public static final String KEY_COLAB = "colab";
    public static final String CONTENT = "Content";
    public static final String COLAB = "Colab";


    private FloatingActionButton infoButton;
    private Button artistButton;
    private Button playlistButton;

    public TextView text;
    public String myresult;
    private UserProfile userProfile;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_info:
                openDialogInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BARRA DE NAVEGACION
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        artistButton = (Button) findViewById(R.id.buttonArtist);
        artistButton.setOnClickListener(v -> artistRecommendation());

        playlistButton = (Button) findViewById(R.id.buttonPlaylist);
        playlistButton.setOnClickListener(v -> openDialogCreatePlaylist());

        //Initializa and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

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


        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        userProfile = new UserProfile(userPreferences.getString("UserToken",null));
        UserProfileBuilder builder = new UserProfileBuilder();
        builder.execute(userProfile);

        ArrayList<Song> TopSongs = userProfile.getTopSongs();
        // System.out.println("Top songs:\n" + TopSongs);
        List<Song> TopSongsAux  = userProfile.getTopSongs().subList(0,10);

        Data myData = new Data.Builder()
                .putString(CONTENT, TopSongsAux.toString())
                .build();
        OneTimeWorkRequest recommendWork = new OneTimeWorkRequest.Builder(ContentWorker.class)
                .setInputData(myData)
                .build();
        WorkManager.getInstance().enqueue(recommendWork);

        WorkManager.getInstance().getWorkInfoByIdLiveData(recommendWork.getId())
                .observe(this, info -> {
                    if (info != null && info.getState().isFinished()) {
                        myresult = info.getOutputData().getString(KEY_CONTENT);
                        System.out.println("Recomendaciones Content: " + myresult);


                    }
                });

        Data Colab = new Data.Builder()
                .putString(COLAB, "")
                .build();
        OneTimeWorkRequest ColabWork = new OneTimeWorkRequest.Builder(ColabWorker.class)
                .setInputData(Colab)
                .build();
        WorkManager.getInstance().enqueue(ColabWork);

        WorkManager.getInstance().getWorkInfoByIdLiveData(ColabWork.getId())
                .observe(this, info -> {
                    if (info != null && info.getState().isFinished()) {
                        myresult = info.getOutputData().getString(KEY_COLAB);
                        System.out.println("Recomendaciones Colab: " + myresult);


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

    public void openDialogCreatePlaylist(){
        CreatePlaylistDialog createPlaylistDialog = new CreatePlaylistDialog();
        createPlaylistDialog.show(getSupportFragmentManager(),"Create Playlist");
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
