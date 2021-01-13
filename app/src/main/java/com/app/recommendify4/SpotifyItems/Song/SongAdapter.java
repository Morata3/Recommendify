package com.app.recommendify4.SpotifyItems.Song;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.recommendify4.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<RecommendedSong> {

    private int layout;
    private Context mContext;

    public SongAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RecommendedSong> objects) {
        super(context, resource, objects);
        this.layout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RecommendedSong song = getItem(position);

        if(convertView == null) convertView = LayoutInflater.from(mContext).inflate(layout, null);

        TextView songName = (TextView) convertView.findViewById(R.id.songName);
        TextView songArtist = (TextView) convertView.findViewById(R.id.songArtist);
        ImageView songImage = (ImageView) convertView.findViewById(R.id.songImage);

        songName.setText(song.getName());
        songArtist.setText(song.getArtistNameList(song.getArtists()));
        Glide.with(convertView).load(song.getCoverURL()).into(songImage);

        return convertView;
    }
}
