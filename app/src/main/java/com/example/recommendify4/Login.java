package com.example.recommendify4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.example.recommendify4.ThreadManagers.ThreadLauncher;
import com.example.recommendify4.UserInfo.Credentials;
import com.example.recommendify4.UserInfo.UserProfile;
import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class Login extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "4b1b0a636b8046a7b305efbf5745c09b";
    private static final String REDIRECT_URI = "recommendify://";
    private Button login_button;
    private Credentials credentials;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Login.context = getApplicationContext();
        setContentView(R.layout.activity_login);
        login_button = (Button) findViewById(R.id.button_login);
        login_button.setOnClickListener(v -> login());

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences userPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        String userProfile = userPreferences.getString("UserProfile",null);
        if(userProfile != null ){
            goMain();
        }
    }

    protected void login(){
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID,
                        AuthorizationResponse.Type.CODE,REDIRECT_URI);
        builder.setScopes(new String[]{
                "user-read-email",
                "ugc-image-upload",
                "user-read-recently-played",
                "user-top-read"
        });
        builder.setShowDialog(true);
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request);
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor login_editor = login.edit();

        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                case CODE:
                    credentials = new Credentials(response.getCode());
                    ThreadLauncher builder_credentials = new ThreadLauncher();
                    builder_credentials.execute(credentials);

                    UserProfile userProfile = new UserProfile(credentials);
                    ThreadLauncher builder_profile = new ThreadLauncher();
                    builder_profile.execute(userProfile);

                    System.out.println("TOKEN:" + credentials.getAccess_token());

                    Gson gson = new Gson();
                    String userProfile_json = gson.toJson(userProfile);
                    login_editor.putString("UserProfile", userProfile_json);
                    login_editor.apply();
                    goMain();
                    break;

                case ERROR:
                    Log.d("Authoritation", "ERROR getting TOKEN");
                    break;

                default:
                    Log.d("Authoritation", response.getType().toString());
                    Log.d("Authoritation","Unexpected error");
                    break;
            }
        }
    }

    public void goMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static Context getContext(){
        return context;
    }

}