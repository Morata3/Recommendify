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


        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
        Python py= Python.getInstance();
        PyObject pyf = py.getModule("FinalRecomendator");
        PyObject obj= pyf.callAttr("rank_song_similarity_by_measure","Bohemian Rhapsody","Queen",2);
        Data outputData = new Data.Builder()
                .putString(KEY_RESULT, obj.toString())
                .build();

    /*int x = getInputData().getInt(KEY_X_ARG, 0);
         Data outputData = new Data.Builder()
                .putInt(KEY_RESULT, result)
                .build();*/
        return Result.success(outputData);

    }
}
