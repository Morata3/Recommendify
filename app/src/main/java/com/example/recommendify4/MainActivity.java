package com.example.recommendify4;

import com.bumptech.glide.Glide;
import com.example.recommendify4.Dialogs.DialogCreatePlaylist;
import com.example.recommendify4.Dialogs.DialogInformation;
import com.example.recommendify4.Dialogs.DialogLoading;
import com.example.recommendify4.Dialogs.DialogLogOut;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.recommendify4.SpotifyItems.Song;
import com.example.recommendify4.UserInfo.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.content.res.TypedArrayUtils;
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

        Gson gson = new Gson();
        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        userProfile = gson.fromJson(userPreferences.getString("UserProfile",null),UserProfile.class);
        ArrayList<Song> TopSongs = userProfile.getTopSongs();
        List<Song> TopSongsAux  = userProfile.getTopSongs().subList(0,10);
        String user_id = userProfile.getUser().getId();
        String user_image_url = userProfile.getUser().getPhoto();

        setupMenu(user_id,user_image_url);
        artistButton = (Button) findViewById(R.id.buttonArtist);
        artistButton.setOnClickListener(v -> artistRecommendation());
        playlistButton = (Button) findViewById(R.id.buttonPlaylist);
        playlistButton.setOnClickListener(v -> openDialogCreatePlaylist());

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

    public void setupMenu(String user_id, String user_image_url){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        TextView user_id_view = (TextView) headerView.findViewById(R.id.nav_header_user_id);
        ImageView user_image_view = (ImageView) headerView.findViewById(R.id.nav_header_user_photo);
        user_id_view.setText(user_id);
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

    @Override
    protected void onStop() {
        super.onStop();
    }

}
