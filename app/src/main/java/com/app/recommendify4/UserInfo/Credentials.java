package com.app.recommendify4.UserInfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.recommendify4.SpotifyApi.RequestSender;

import org.json.JSONObject;

import java.util.Date;

public class Credentials implements Runnable, Parcelable{

    private static final String CLIENT_ID = "4b1b0a636b8046a7b305efbf5745c09b";
    private static final String CLIENT_SECRET = "9f3ed9fe6167487f8f129a5ef0ad4757";
    private static final String REDIRECT_URI = "recommendify://";
    private Date time_to_expire;
    private String access_token;
    private String refresh_token;
    private String code;

    public Credentials(String code){
        this.code = code;
    }

    public String getAccess_token() { return access_token; }

    public String getRefresh_token(){ return refresh_token; }

    public Date getTime_to_expire(){return time_to_expire; }

    private void setTokens() {
        try{
            String tokens_response = RequestSender.getTokens(code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI);
            if(!tokens_response.equals("ERROR")){
                JSONObject jsonToken = new JSONObject(tokens_response);
                this.access_token = jsonToken.getString("access_token");
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
            String tokens_response = RequestSender.getAccessToken(refresh_token,CLIENT_ID,CLIENT_SECRET);
            if(!tokens_response.equals("ERROR")){
                JSONObject jsonToken = new JSONObject(tokens_response);
                this.access_token = jsonToken.getString("access_token");
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

    //PARCELABLE IMPLEMENTATION
    private Credentials(Parcel in) {
        time_to_expire = new Date();
        access_token = in.readString();
        refresh_token = in.readString();
        time_to_expire = (Date) in.readSerializable();
    }

    public static final Parcelable.Creator<Credentials> CREATOR
            = new Parcelable.Creator<Credentials>() {
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(refresh_token);
        dest.writeSerializable(time_to_expire);
    }

    @Override
    public void run() {
        setTokens();
    }
}
