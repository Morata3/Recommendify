package com.example.recommendify4.RecomThreads;
import androidx.work.Data;
import androidx.work.ListenableWorker;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.recommendify4.SpotifyApi.RequestSender;
import com.example.recommendify4.SpotifyApi.ResponseProcessor;
import com.example.recommendify4.SpotifyItems.Artist;
import com.example.recommendify4.SpotifyItems.Song;
import com.example.recommendify4.SpotifyItems.User;
import java.util.ArrayList;

public class ContentThread implements Runnable{

    private String recomendations="";
    private ArrayList<Song> topSongs;

    public ContentThread(ArrayList<Song> topSongs){

        this.topSongs = topSongs;

    }



    @Override
    public void run() {
        while(topSongs.size() > 0 ) {
            if (!Python.isStarted()) {
             // Python.start(new AndroidPlatform(context));  encontrar context
            }
            ArrayList<Song> TopSongs = new ArrayList<>();

            Python py = Python.getInstance();
            PyObject pyf = py.getModule("FinalRecomendator");

            PyObject obj = pyf.callAttr("rank_song_similarity_by_measure", TopSongs.toString(), 2);
            String recommendations = obj.toString();

            recommendations = recommendations.substring(1,recommendations.length() - 1);
            String[] Recoms = recommendations.split("],");

            ArrayList<Song> Recommendations = new ArrayList<>();
            for(int k = 1; k<16;k++) {
                String title = Recoms[k].split(",")[0].substring(3,Recoms[k].split(",")[0].length()-1);
                String id =Recoms[k].split(",")[1].substring(2,Recoms[k].split(",")[1].length()-1);
                String Artist = Recoms[k].split(",")[2].substring(2,Recoms[k].split(",")[2].length()-1);
                Recommendations.add(new Song(title,Artist,id));
            }

            String WasteIds = Recommendations.get(14).getId();

            String[] wasteids = WasteIds.split("/");

            for (int i = 0; i < TopSongs.size(); i++) {
                for(int j = 0; j < wasteids.length; j++) {
                    if (TopSongs.get(i).getId() != null && TopSongs.get(i).getId().equals(wasteids[j])) {
                        TopSongs.remove(i);
                    }
                }
            }
        }
    }
}




