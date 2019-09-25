package com.rrooaarr.werkstueck.util;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;


public class DialogBuilder {

    public static AlertDialog buildDialog(final Activity activity, final String dialog_message, final String dialog_title, final String positiveButtonText, final DialogInterface.OnClickListener clickListener) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(dialog_message)
                .setTitle(dialog_title);
        if (positiveButtonText != null && !positiveButtonText.isEmpty() && clickListener != null) {
            builder.setPositiveButton(positiveButtonText, clickListener);
        }

        // 3. Get the AlertDialog from create()
        return builder.create();
    }

    public static AlertDialog buildDefaultDialog(final Activity activity, final String dialog_message, final String dialog_title) {

        return DialogBuilder.buildDialog(activity, dialog_message, dialog_title, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}
