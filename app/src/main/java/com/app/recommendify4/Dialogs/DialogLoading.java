package com.app.recommendify4.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.app.recommendify4.R;

public class DialogLoading extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

//    private AlertDialog dialog;
//
//    public static DialogLoading newInstance(){
//        DialogLoading frag = new DialogLoading();
//        return frag;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity()).create();
//    }
//
//    public void startLoadingAnimation(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//        LayoutInflater inflater = mContext.getLayoutInflater();
//        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
//        builder.setCancelable(false);
//
//        dialog = builder.create();
//        dialog.show();
//    }

//    public void dismiss(){
//        dialog.dismiss();
//    }

}
