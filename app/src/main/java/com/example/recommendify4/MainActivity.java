package com.example.recommendify4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "4b1b0a636b8046a7b305efbf5745c09b";
    private static final String REDIRECT_URI = "recommendify://";
    private FloatingActionButton infoButton;
    private Button artistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoButton = (FloatingActionButton) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> openDialogInfo());

        artistButton = (Button) findViewById(R.id.buttonArtist);
        artistButton.setOnClickListener(v -> artistRecommendation());

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
    }

    @Override
    protected void onStart() {
        super.onStart();
//        AuthorizationRequest.Builder builder =
//                new AuthorizationRequest.Builder(CLIENT_ID,
//                        AuthorizationResponse.Type.TOKEN,REDIRECT_URI);
//        builder.setScopes(new String[]{
//                "user-read-email",
//                "ugc-image-upload"
//        });
//        AuthorizationRequest request = builder.build();
//
//        AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request);

    }

    public void openDialogInfo(){
        DialogInfo dialogInfo = new DialogInfo();
        dialogInfo.show(getSupportFragmentManager(),"Dialog Information");
    }

    public void artistRecommendation(){
        Intent intent = new Intent(this, ArtistRecommendation.class);
        startActivity(intent);
    }

//    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
//        super.onActivityResult(requestCode,resultCode,intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
//
//            switch (response.getType()) {
//                // Response was successful and contains auth token
//                case TOKEN:
//
//                    Log.d("MainActivity","USER TOKEN:" + response.getAccessToken());
//                    break;
//
//                // Auth flow returned an error
//                case ERROR:
//                    // Handle error response
//                    break;
//
//                // Most likely auth flow was cancelled
//                default:
//                    // Handle other cases
//            }
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
    }


}
