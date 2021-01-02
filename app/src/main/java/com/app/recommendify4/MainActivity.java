package com.app.recommendify4;

import com.app.recommendify4.Dialogs.DialogCreatePlaylist;
import com.app.recommendify4.Dialogs.DialogInformation;
import com.app.recommendify4.Dialogs.DialogLoading;
import com.app.recommendify4.Dialogs.DialogLogOut;
import com.app.recommendify4.Fragments.FragmentHybrid;
import com.app.recommendify4.Fragments.FragmentSoulmateArtist;
import com.app.recommendify4.RecomThreads.CollaborativeCallback;
import com.app.recommendify4.RecomThreads.ContentCallback;
import com.app.recommendify4.RecomThreads.ContentThread;
import com.app.recommendify4.SpotifyItems.Artist.UserArtist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.SpotifyItems.Song.UserSong;
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
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.app.recommendify4.UserInfo.Recommendations;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private FragmentTransaction fragmentTransaction;
    private Fragment fragmentLauncher;
    private FragmentSong fragmentSong;
    private FragmentHybrid fragmentHybrid;
    private FragmentSoulmateArtist fragmentSoulmateArtist;
    public TextView text;
    private int index = 0;

    private UserProfile userProfile;
    private Credentials credentials;

    private Recommendations userRecommendations;
    private int lastSongProcessed = 0;
    private int lastArtistProcessed = 0;
    private final ThreadPoolExecutor threadPoolExecutor = RecomThreadPool.getThreadPoolExecutor();
    private final ContentCallback contentThreadCallback = new ContentCallback() {
        @Override
        public synchronized void onComplete(ArrayList<RecommendedSong> recommendations) {
            userRecommendations.addSongRecommendations(recommendations);
        }
    };
    private final CollaborativeCallback collaborativeThreadCallback = new CollaborativeCallback() {
        @Override
        public void onComplete(ArrayList<com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist> recommendations) {
            userRecommendations.addArtistRecommendations(recommendations);
        }
    };

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

        setUpUserInfo();
        setupMenu(userProfile.getUser().getName(),userProfile.getUser().getImageURL());
        setupFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.advanced:
                        Intent intent = new Intent(getApplicationContext(), Advanced.class);
                        intent.putExtra("credentials", credentials);
                        startActivity(intent);

                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.history:
                        System.out.println("HISTORIAL DE CANCIONS: ");
                        for(RecommendedSong song : userRecommendations.getSongsShown()) System.out.println(song);

                    /*startActivity(new Intent(getApplicationContext()
                            , History.class));*/
                        //overridePendingTransition(0, 0);
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
        //setUpUserInfo();
    }


    public void openDialogInformation() {
        DialogInformation dialogInformation = new DialogInformation();
        dialogInformation.show(getSupportFragmentManager(), "Dialog Information");
    }
    public void openDialogCreatePlaylist(){
        DialogCreatePlaylist dialogCreatePlaylist = new DialogCreatePlaylist();
        Bundle arguments = new Bundle();
        arguments.putString("userId", userProfile.getUser().getId());
        arguments.putParcelableArrayList("playlistSongs", userRecommendations.getSongsRecommendations());
        arguments.putParcelable("credentials", userProfile.getCredentials());
        dialogCreatePlaylist.setArguments(arguments);
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
                if(userRecommendations.getSongsRecommendations() != null && userRecommendations.getSongsRecommendations().size() > 0){
                    fragmentSong = FragmentSong.newInstance(userRecommendations.getSongsRecommendations(), userRecommendations.getSongsShown(), userProfile.getTopSongs(),credentials, lastSongProcessed);
                    fragmentTransaction.replace(R.id.fragmentMain,fragmentSong);
                    fragmentTransaction.addToBackStack(null);
                }else System.out.println("Recommendations not yet ready ");
                break;
            case R.id.buttonSoulmate:
                //soulmateArtistRecommendation();
                if(userRecommendations.getArtistRecommendations() != null && userRecommendations.getArtistRecommendations().size() > 0){
                    fragmentSoulmateArtist = FragmentSoulmateArtist.newInstance(userRecommendations.getArtistRecommendations(), userRecommendations.getArtistsShown(), userProfile.getTopArtists(),credentials, lastArtistProcessed);
                    fragmentTransaction.replace(R.id.fragmentMain, fragmentSoulmateArtist);
                    fragmentTransaction.addToBackStack(null);
                }else System.out.println("Artist recommendations not yet ready");
                break;
            case R.id.buttonPlaylist:
                System.out.println(credentials);
                openDialogCreatePlaylist();
                break;
            case R.id.buttonArtist:
                artistRecommendation();
                break;
            case R.id.buttonHybrid:
                System.out.println("Llego aqí ni que sea");
                if(this.userRecommendations.getArtistRecommendations() != null && this.userRecommendations.getArtistRecommendations().size() != 0 &&
                        this.userRecommendations.getSongsRecommendations() != null && this.userRecommendations.getArtistRecommendations().size() != 0){

                    fragmentHybrid = FragmentHybrid.newInstance(getHybridSongs(),credentials);
                    fragmentTransaction.replace(R.id.fragmentMain,fragmentHybrid);
                    fragmentTransaction.addToBackStack(null);
                }else System.out.println("Recommendations not yet ready ");
                break;

        }
        fragmentTransaction.commit();
    }

    public ArrayList<RecommendedSong> getHybridSongs(){
        if(this.userRecommendations.getArtistRecommendations() == null || this.userRecommendations.getArtistRecommendations().size() == 0 ||
                this.userRecommendations.getSongsRecommendations() == null || this.userRecommendations.getSongsRecommendations().size() == 0)
            return new ArrayList<>();
        else{
            ArrayList<RecommendedSong> HybridRecommendations =  this.userRecommendations.getSongsRecommendations();
            for(RecommendedSong song : HybridRecommendations ){
                for(com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artist : this.userRecommendations.getArtistRecommendations()){

                    //if(song.getartistsString().equals(artist.getName())){
                    //COMO ESTA AHORA AS CANCIONS QUE SALEN DE FinalRecommendator solo teñen un artista (os artistas que colaboran aparecen no titulo
                    if(song.getArtists().get(0).getName().equals(artist.getName())){
                        song.setCoincidence(song.getCoincidence()+1);

                    }
                }
            }
            Collections.sort(HybridRecommendations, RecommendedSong.Coincidences);

            return HybridRecommendations;
        }


    }

    public void setupFragment(){
        fragmentLauncher = new FragmentLauncher();
        fragmentSong = new FragmentSong();
        fragmentSoulmateArtist = new FragmentSoulmateArtist();
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
        if(userRecommendations.getArtistRecommendations() != null && userRecommendations.getArtistRecommendations().size() > 0){
            com.app.recommendify4.SpotifyItems.Artist.RecommendedArtist artistToRecommend = userRecommendations.getArtistRecommendations().get(0);
            intent.putExtra("artistToRecommend", artistToRecommend);
            userRecommendations.moveToHistory(artistToRecommend);

        }
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setUpUserInfo(){
        userProfile = getUserProfile();
        credentials = userProfile.getCredentials();

        if(userRecommendations == null){
            userRecommendations = new Recommendations();
            ArrayList<UserSong> userTopSongs = userProfile.getTopSongs();
            ArrayList<UserArtist> userTopArtists = userProfile.getTopArtists();

            for (UserSong song : userTopSongs.subList(lastSongProcessed, lastSongProcessed + 5)) threadPoolExecutor.execute(new ContentThread(song, contentThreadCallback, credentials));
            for (UserArtist artist: userTopArtists.subList(lastArtistProcessed, lastArtistProcessed + 5)) threadPoolExecutor.execute(new CollaborativeThread(artist, collaborativeThreadCallback, credentials));

            lastSongProcessed = lastSongProcessed + 5;
            lastArtistProcessed+=  5;
        }


    }

    private UserProfile getUserProfile(){
        if(this.userProfile == null){
            Gson gson = new Gson();
            SharedPreferences userPreferences = getSharedPreferences(Login.PREFERENCES_NAME, MODE_PRIVATE);
            return gson.fromJson(userPreferences.getString(Login.PREFERENCES_USER,null),UserProfile.class);

        }
        else return this.userProfile;
    }


}