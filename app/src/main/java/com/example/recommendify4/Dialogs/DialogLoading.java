package com.example.recommendify4.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.recommendify4.R;

public class DialogLoading {

    private Activity activity;
    private AlertDialog dialog;

    public DialogLoading(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingAnimation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

}
