package com.app.recommendify4;

import com.app.recommendify4.Dialogs.DialogCreatePlaylist;
import com.app.recommendify4.Dialogs.DialogInformation;
import com.app.recommendify4.Dialogs.DialogLoading;
import com.app.recommendify4.Dialogs.DialogLogOut;
import com.app.recommendify4.RecomThreads.CollaborativeCallback;
import com.app.recommendify4.RecomThreads.ContentCallback;
import com.app.recommendify4.RecomThreads.ContentThread;
import com.app.recommendify4.SpotifyItems.Artist;
import com.app.recommendify4.SpotifyItems.Song;
import com.app.recommendify4.ThreadManagers.RecomThreadPool;
import com.app.recommendify4.UserInfo.UserProfile;
import com.bumptech.glide.Glide;

import com.app.recommendify4.RecomThreads.CollaborativeThread;

import com.app.recommendify4.Fragments.FragmentLauncher;
import com.app.recommendify4.Fragments.FragmentSong;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.recommendify4.UserInfo.Credentials;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.app.recommendify4.UserInfo.UserRecommendations;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private FragmentTransaction fragmentTransaction;
    private Fragment fragmentLauncher;
    private Fragment fragmentSong;

    public TextView text;

    private UserProfile userProfile;
    private ArrayList<Song> songRecommendations;
    private ArrayList<Artist> artistRecommendations;

    private ArrayList<Song> songsRecommendations = new ArrayList<>();

    private UserRecommendations profileRecommend;
    private Credentials credentials;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info:
                openDialogInformation();
                return true;
            case R.id.menu_aboutMe:
                openDialogAboutMe();
                return true;
            case R.id.menu_logout:
                openDialogLogOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userProfile = getUserProfile();
        credentials = userProfile.getCredentials();

        songRecommendations = getRecommendationsList();
        artistRecommendations = getArtistRecommendationsList();

        setupMenu(userProfile.getUser().getName(),userProfile.getUser().getImageURL());
        setupFragment();


        if(songsRecommendations.size() == 0) {
            ArrayList<Song> userRecentlyPlayedSongs = userProfile.getRecentlyPlayedSongs();
            ArrayList<Song> userTopSongs = userProfile.getTopSongs();

            ThreadPoolExecutor executor = RecomThreadPool.getThreadPoolExecutor();
            for (Song song : userRecentlyPlayedSongs)
                executor.execute(
                        new ContentThread(song, new ContentCallback() {
                            @Override
                            public synchronized void onComplete(ArrayList<Song> recommendations) {
                                songRecommendations.addAll(recommendations);
                                songsRecommendations.addAll(recommendations);
                            }
                        })
                );

            for (Song song : userTopSongs)
                executor.execute(
                        new ContentThread(song, new ContentCallback() {
                            @Override
                            public synchronized void onComplete(ArrayList<Song> recommendations) {
                                songRecommendations.addAll(recommendations);
                                songsRecommendations.addAll(recommendations);
                            }
                        }));
        }

        if(artistRecommendations.size() == 0) {
            ArrayList<Artist> userTopArtists = userProfile.getTopArtists();
            ThreadPoolExecutor executor = RecomThreadPool.getThreadPoolExecutor();
            for (Artist artist : userTopArtists)
                executor.execute(
                        new CollaborativeThread(artist, new CollaborativeCallback() {
                            @Override
                            public synchronized void onComplete(ArrayList<Artist> recommendations) {
                                artistRecommendations.addAll(recommendations);
                            }
                        }, userProfile)
                );
        }

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


    public void openDialogInformation() {
        DialogInformation dialogInformation = new DialogInformation();
        dialogInformation.show(getSupportFragmentManager(), "Dialog Information");
    }
    public void openDialogCreatePlaylist(){
        DialogCreatePlaylist dialogCreatePlaylist = new DialogCreatePlaylist();
        dialogCreatePlaylist.show(getSupportFragmentManager(),"Create Playlist");
    }
    public void openDialogLogOut(){
        DialogLogOut dialogLogOut = new DialogLogOut();
        dialogLogOut.show(getSupportFragmentManager(),"LogOut");
    }
    public void openDialogAboutMe(){
        DialogLoading dialogLoading = new DialogLoading(MainActivity.this);
        dialogLoading.startLoadingAnimation();
        dialogLoading.dismiss();
    }


    public void onClick(View view){
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.buttonShuffle:
                if(songsRecommendations != null && songsRecommendations.size() > 0){
                    fragmentSong = FragmentSong.newInstance(songsRecommendations,credentials);
                    fragmentTransaction.replace(R.id.fragmentMain,fragmentSong);
                    fragmentTransaction.addToBackStack(null);
                }else System.out.println("Recommendations not yet ready ");
                break;
            case R.id.buttonSoulmate:
                soulmateArtistRecommendation();
                break;
            case R.id.buttonPlaylist:
                openDialogCreatePlaylist();
                break;
            case R.id.buttonArtist:
                artistRecommendation();
                break;

        }
        fragmentTransaction.commit();
    }

    public void setupFragment(){
        fragmentLauncher = new FragmentLauncher();
        fragmentSong = new FragmentSong();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentMain,fragmentLauncher).commit();
    }

    public void setupMenu(String user_name, String user_image_url){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        TextView user_name_view = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        ImageView user_image_view = (ImageView) headerView.findViewById(R.id.nav_header_user_photo);

        user_name_view.setText(user_name);
        if(user_image_url != null){
            System.out.println("Setting image");
            Glide.with(this).load(user_image_url).into(user_image_view);
        }
        else System.out.println("User has no photo");

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void artistRecommendation(){
        Intent intent = new Intent(this, ArtistRecommendation.class);
        startActivity(intent);
    }

    private void soulmateArtistRecommendation(){
        Intent intent = new Intent(this, SoulmateArtistRecommendation.class);
        //intent.putExtra("songsToRecommend", userRecommendations);
        if(artistRecommendations != null && artistRecommendations.size() > 0){
            Artist artistToRecommend = artistRecommendations.get(0);
            intent.putExtra("artistToRecommend", artistRecommendations.get(0));
            artistRecommendations.remove(artistToRecommend);

        }
        startActivity(intent);
    }

    private UserProfile getUserProfile(){
        if(this.userProfile == null){
            Gson gson = new Gson();
            SharedPreferences userPreferences = getSharedPreferences(Login.PREFERENCES_NAME, MODE_PRIVATE);
            return gson.fromJson(userPreferences.getString(Login.PREFERENCES_USER,null),UserProfile.class);

        }
        else return this.userProfile;
    }

    private ArrayList<Song> getRecommendationsList(){
        if(this.songRecommendations == null || this.songRecommendations.size() == 0) return new ArrayList<>();
        else return this.songRecommendations;
    }

    private ArrayList<Artist> getArtistRecommendationsList(){
        if(this.artistRecommendations == null || this.artistRecommendations.size() == 0) return new ArrayList<>();
        else return this.artistRecommendations;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
