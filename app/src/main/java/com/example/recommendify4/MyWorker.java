package com.example.recommendify4;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;


public class MyWorker extends Worker {
    public static final String KEY_RESULT = "result";
    public static final String CANCIONES = "Canciones";


    private Context context;

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String TopSongs = getInputData().getString(CANCIONES);
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("FinalRecomendator");

        PyObject obj = pyf.callAttr("rank_song_similarity_by_measure", TopSongs, 2);
        Data outputData = new Data.Builder()
                .putString(KEY_RESULT, obj.toString())
                .build();

    /*int x = getInputData().getInt(KEY_X_ARG, 0);
         Data outputData = new Data.Builder()
                .putInt(KEY_RESULT, result)
                .build();*/
        return Result.success(outputData);

    }

    public String GetArtist(String TopSongs, int i) {
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        System.out.println("Songs: " + TopSongs);
        System.out.println("Song: " + songs[i]);
        String[] Artists = songs[i].split("=");
        String[] FinalArtist = Artists[5].split(",");
        FinalArtist[0] = FinalArtist[0].substring(1, FinalArtist[0].length() - 1);
        return FinalArtist[0];
    }

    public String GetSong(String TopSongs, int i) {
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        String[] SongTitle = songs[i].split("name|=");
        String[] FinalSong = SongTitle[2].split(",");
        FinalSong[0] = FinalSong[0].substring(1, FinalSong[0].length() - 1);
        return FinalSong[0];
    }

    public String GetId(String TopSongs, int i) {
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        String[] ID = songs[i].split("=");
        String[] FinalID = ID[3].split(",");
        FinalID[0] = FinalID[0].substring(1, FinalID[0].length() - 1);

        return FinalID[0];
    }

}