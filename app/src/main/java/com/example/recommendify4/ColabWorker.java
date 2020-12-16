package com.example.recommendify4;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;


public class ColabWorker extends Worker {
    public static final String KEY_COLAB = "colab";
    public static final String COLAB = "Colab";


    private Context context;

    public ColabWorker(
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

        Python py = Python.getInstance();
        PyObject pyf = py.getModule("full_recommender");
        PyObject obj = pyf.callAttr("recommend");
        System.out.println("CAMILO: " +obj);
        Data outputData = new Data.Builder()
                .putString(KEY_COLAB, obj.toString())
                .build();
        
        return Result.success(outputData);

    }


}