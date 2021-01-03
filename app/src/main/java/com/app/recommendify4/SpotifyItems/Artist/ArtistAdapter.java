package com.app.recommendify4.SpotifyItems.Artist;

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

public class ArtistAdapter extends ArrayAdapter<RecommendedArtist> {

    private int layout;
    private Context mContext;

    public ArtistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RecommendedArtist> objects) {
        super(context, resource, objects);
        this.layout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RecommendedArtist artist = getItem(position);

        if(convertView == null) convertView = LayoutInflater.from(mContext).inflate(layout, null);

        TextView songArtist = (TextView) convertView.findViewById(R.id.artistName);
        ImageView songImage = (ImageView) convertView.findViewById(R.id.artistImage);

        songArtist.setText(artist.getName());
        Glide.with(convertView).load(artist.getImage()).into(songImage);

        return convertView;
    }

}
