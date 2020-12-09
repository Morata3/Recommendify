package com.example.recommendify4.UserInfo;

import java.util.concurrent.Executor;

public class UserProfileBuilder implements Executor {

    @Override
    public void execute(Runnable command) {
        Thread userProfileThread = new Thread(command);
        try {
            userProfileThread.start();
            userProfileThread.join();
        } catch (InterruptedException e) {
            System.out.println("Error building profile");
            e.printStackTrace();
        }
    }
}
