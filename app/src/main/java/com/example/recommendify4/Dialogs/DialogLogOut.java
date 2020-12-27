package com.example.recommendify4.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.recommendify4.Login;
import com.example.recommendify4.MainActivity;
import com.example.recommendify4.R;

public class DialogLogOut extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_log_out,null);
        builder.setView(view)
                .setTitle("Log out")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    public void logout(){
        Context context = Login.getContext();
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor login_editor = login.edit();
        login_editor.clear();
        login_editor.apply();

        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
    }
}
