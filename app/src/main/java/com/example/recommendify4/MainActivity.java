package com.example.recommendify4;

<<<<<<< Updated upstream
import com.example.recommendify4.Dialogs.DialogCreatePlaylist;
import com.example.recommendify4.Dialogs.DialogInformation;
import com.example.recommendify4.Dialogs.DialogLogOut;
=======
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
>>>>>>> Stashed changes
import com.example.recommendify4.SpotifyItems.Song;
import com.example.recommendify4.UserInfo.UserProfile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
<<<<<<< Updated upstream
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
=======
import androidx.core.content.res.TypedArrayUtils;
>>>>>>> Stashed changes
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String KEY_CONTENT = "content";
    public static final String KEY_COLAB = "colab";
    public static final String CONTENT = "Content";
    public static final String COLAB = "Colab";


    private DrawerLayout drawer;
    private Button artistButton;
    private Button playlistButton;

    public TextView text;
    public String myresult;
    private UserProfile userProfile;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info:
                openDialog("information");
                return true;
            case R.id.menu_aboutMe:
                return true;
            case R.id.menu_logout:
                openDialog("logout");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        userProfile = gson.fromJson(userPreferences.getString("UserProfile",null),UserProfile.class);
        ArrayList<Song> TopSongs = userProfile.getTopSongs();
        List<Song> TopSongsAux  = userProfile.getTopSongs().subList(0,10);


        artistButton = (Button) findViewById(R.id.buttonArtist);
        artistButton.setOnClickListener(v -> artistRecommendation());
        playlistButton = (Button) findViewById(R.id.buttonPlaylist);
        playlistButton.setOnClickListener(v -> openDialog("playlist"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
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

<<<<<<< Updated upstream
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
=======

        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        userProfile = new UserProfile(userPreferences.getString("UserToken",null));
        ThreadLauncher builder = new ThreadLauncher();
        builder.execute(userProfile);

        ArrayList<Song> TopSongs = userProfile.getTopSongs();

>>>>>>> Stashed changes
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void openDialog(String dialog){
        switch (dialog) {
            case "information":
                DialogInformation dialogInformation = new DialogInformation();
                dialogInformation.show(getSupportFragmentManager(), "Dialog Information");
                break;
            case "playlist":
                DialogCreatePlaylist dialogCreatePlaylist = new DialogCreatePlaylist();
                dialogCreatePlaylist.show(getSupportFragmentManager(),"Create Playlist");
                break;
            case "logout":
                DialogLogOut dialogLogOut = new DialogLogOut();
                dialogLogOut.show(getSupportFragmentManager(),"LogOut");
                break;
        }
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
