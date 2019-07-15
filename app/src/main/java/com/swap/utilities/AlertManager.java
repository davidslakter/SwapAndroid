package com.swap.utilities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.swap.R;


public class AlertManager {
    public AlertManager() {
    }

    public static void showToast(Context oContext, String message) {
        Toast.makeText(oContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showAlertDialog(Context oContext, String title, String message) {
        Builder alertDialogBuilder = new Builder(oContext);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(oContext.getString(R.string.ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}