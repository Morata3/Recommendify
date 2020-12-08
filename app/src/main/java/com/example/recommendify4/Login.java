package com.example.recommendify4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class Login extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "4b1b0a636b8046a7b305efbf5745c09b";
    private static final String REDIRECT_URI = "recommendify://";
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_button = (Button) findViewById(R.id.button_login);
        login_button.setOnClickListener(v -> login());

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        if(userPreferences.getString("UserToken",null) != null ) goMain();
    }

    protected void login(){

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID,
                        AuthorizationResponse.Type.TOKEN,REDIRECT_URI);
        builder.setScopes(new String[]{
                "user-read-email",
                "ugc-image-upload",
                "user-read-recently-played",
                "user-top-read"


        });
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request);
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        //We store a token using SharedPreferences
        SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor login_editor = login.edit();

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("MainActivity","TOKEN Expire:" + response.getExpiresIn());
                    login_editor.putString("UserToken",response.getAccessToken());
                    login_editor.commit();

                    goMain();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    public void goMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}