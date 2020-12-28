package com.example.recommendify4.ThreadManagers;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RecomThreadPool {

    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int KEEP_ALIVE_TIME = 10000;
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MICROSECONDS;

    private ThreadPoolExecutor threadsExecutor;
    private static RecomThreadPool instance;

    private RecomThreadPool (){
        this.threadsExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES*2,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                new LinkedBlockingDeque<Runnable>(100)
        );
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        if(instance == null) instance = new RecomThreadPool();
        return instance.threadsExecutor;
    }


}