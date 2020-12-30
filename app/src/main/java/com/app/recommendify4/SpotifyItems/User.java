package com.app.recommendify4.SpotifyItems;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class User {

    private String id;
    private String name;
    private String imageURL;

    public User(){

    }

    public User(JSONObject userInfoJson, String imageURL) throws JSONException {
        this.id = userInfoJson.getString("id");
        this.name = userInfoJson.getString("display_name");
        setPhoto(imageURL);
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getImageURL() {return imageURL; }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setPhoto(String url) {

        if(url != null){
            System.out.println("Setting image");
            this.imageURL = url;
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
