package com.app.recommendify4.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.app.recommendify4.SpotifyApi.RequestSender;
import com.app.recommendify4.SpotifyItems.Playlist;
import com.app.recommendify4.SpotifyItems.Song.RecommendedSong;
import com.app.recommendify4.ThreadManagers.ThreadLauncher;
import com.app.recommendify4.UserInfo.Credentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DialogCreatePlaylist extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle receivedArguments = getArguments();
        ArrayList<RecommendedSong> playlistSongs = receivedArguments.getParcelableArrayList("playlistSongs");
        String userId = receivedArguments.getString("userId");
        Credentials credentials = receivedArguments.getParcelable("credentials");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle("New Playlist");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = input.getText().toString()+" (by Recommendify4)";
                System.out.println(playlistName);
                Playlist playlist = new Playlist(playlistName, playlistSongs);
                ThreadLauncher launcher = new ThreadLauncher();
                launcher.execute(new Runnable() {
                    @Override
                    public void run() {
                        String response = RequestSender.createPlaylist(credentials, playlistName, userId);
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            playlist.setId(responseJSON.getString("id"));
                            RequestSender.addSongsToPlaylist(credentials, playlistSongs, playlist.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

}
