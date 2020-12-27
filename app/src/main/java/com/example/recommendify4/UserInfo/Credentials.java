package com.example.recommendify4.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.recommendify4.Login;
import com.example.recommendify4.SpotifyApi.RequestSender;

import org.json.JSONObject;

import java.util.Date;

public class Credentials implements Runnable{

    private static final String CLIENT_ID = "4b1b0a636b8046a7b305efbf5745c09b";
    private static final String CLIENT_SECRET = "9f3ed9fe6167487f8f129a5ef0ad4757";
    private static final String REDIRECT_URI = "recommendify://";
    private static Date time_to_expire;
    private String acces_token;
    private String refresh_token;
    private String token;

    public Credentials(String token){
        this.token = token;
    }

    public String getAcces_token() { return acces_token; }

    public String getRefresh_token(){ return refresh_token; }

    public Date getTime_to_expire(){return time_to_expire; }

    private void setTokens() {
        try{
            String tokens_response = RequestSender.getTokens(token,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI);
            if(!tokens_response.equals("ERROR")){
                JSONObject jsonToken = new JSONObject(tokens_response);
                this.acces_token = jsonToken.getString("access_token");
                this.refresh_token = jsonToken.getString("refresh_token");
                setTimeExpire(jsonToken.getInt("expires_in"));
            }
        }catch (Exception e){
            System.out.println("Error setting access token");
            System.out.println(e.getMessage());
        }
    }

    public void updateToken(){
        try{
            String tokens_response = RequestSender.getAccessToken(token,CLIENT_ID,CLIENT_SECRET);
            if(!tokens_response.equals("ERROR")){
                JSONObject jsonToken = new JSONObject(tokens_response);
                this.acces_token = jsonToken.getString("access_token");
                setTimeExpire(jsonToken.getInt("expires_in"));
                System.out.println("TOKEN UPDATED");
            }
        }catch (Exception e){
            System.out.println("Error updating access token");
            System.out.println(e.getMessage());
        }
    }

    public void setTimeExpire(int expire_in){
        Date now = new Date();
        time_to_expire = new Date(now.getTime() + expire_in*1000);
        System.out.println("TIME EXPIRE: "+time_to_expire);
    }

    public void checkTokenExpiration() {
        Date now = new Date();
        if (((time_to_expire.getTime() / 1000) - (now.getTime() / 1000)) < 60) updateToken();
    }


    @Override
    public void run() {
        setTokens();
    }
}
