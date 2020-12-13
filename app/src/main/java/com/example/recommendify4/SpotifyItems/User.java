package com.example.recommendify4.SpotifyItems;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private String id;
    private String name;

    public User(){

    }

    public User(JSONObject userInfoJson) throws JSONException {
        this.id = userInfoJson.getString("id");
        this.name = userInfoJson.getString("display_name");
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
