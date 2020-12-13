package com.example.recommendify4;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.recommendify4.UserInfo.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        String TopSongs = getInputData().getString(CANCIONES);
    /*    String idValidSong = null;
        try {
            idValidSong = GetValidSong(TopSongs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("IdValidSong: " + idValidSong);*/

        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
        Python py= Python.getInstance();
        PyObject pyf = py.getModule("FinalRecomendator");

        PyObject obj= pyf.callAttr("rank_song_similarity_by_measure",TopSongs,2);
        Data outputData = new Data.Builder()
                .putString(KEY_RESULT, obj.toString())
                .build();

    /*int x = getInputData().getInt(KEY_X_ARG, 0);
         Data outputData = new Data.Builder()
                .putInt(KEY_RESULT, result)
                .build();*/
        return Result.success(outputData);

    }

    public String GetArtist(String TopSongs,int i){
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        System.out.println("Songs: " + TopSongs);
        System.out.println("Song: " + songs[i]);
        String[] Artists = songs[i].split("=");
        String[] FinalArtist = Artists[5].split(",");
        FinalArtist[0] = FinalArtist[0].substring( 1, FinalArtist[0].length() - 1 );
        return FinalArtist[0];
    }
    public String GetSong(String TopSongs,int i){
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        String[] SongTitle = songs[i].split("name|=");
        String[] FinalSong = SongTitle[2].split(",");
        FinalSong[0] = FinalSong[0].substring( 1, FinalSong[0].length() - 1 );
        return FinalSong[0];
    }
    public String GetId(String TopSongs,int i){
        String[] songs = TopSongs.split("Song |Song |Song|Song |Song|Song|Song|Song|Song");
        String[] ID = songs[i].split("=");
        String[] FinalID = ID[3].split(",");
        FinalID[0] = FinalID[0].substring( 1, FinalID[0].length() - 1 );

        return FinalID[0];
    }

   /* @RequiresApi(api = Build.VERSION_CODES.O)
    public String GetValidSong(String TopSongs) throws IOException {
        int getOut = 0;
        String Txt = readFileAsString();
        String[] ids = Txt.split(",");
        String id = "";

        while(getOut==0){
            for(int j = 1; j < 10; j++){
                id = GetId(TopSongs,j);
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i].equals(i)){
                        String Artist = GetArtist(TopSongs,j);
                        String Song = GetSong(TopSongs,j);
                        System.out.println("Id: " + id);
                        System.out.println("Title: " + Song);
                        System.out.println("Artist " + Artist );
                        getOut=1;

                    }
                 }
            }
        }


        return id;
    }

   /* @RequiresApi(api = Build.VERSION_CODES.O)
    public String readFileAsString() throws IOException {
        String line="";
        try {
            System.out.println("SAAAAAAAAAAAAAAAAAAA");
            String file = "res/raw/ids.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        line = new String(buffer, "UTF-8");
            System.out.println("SAAAAAAAAAAAAAAAAAAAe");

        } catch (IOException ex) {
        ex.printStackTrace();
        return null;
    }
        return line;

    }*/
}
