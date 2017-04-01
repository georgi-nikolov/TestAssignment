package com.example.xcomputers.testassignment.util;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * Utility class for providing an alert dialog
 */
public class AlertDialogUtil {

    /**
     * Utility method that displays an alert dialog
     *
     * @param context         A context to show the dialog
     * @param positiveClick   onClick implementation for the positive button
     * @param negativeClick   onClick implementation for the negative button
     * @param message         the message to be set to the dialog
     * @param positiveBtnText the text for the positive button
     * @param negativeBtnText the text for the negative button
     */
    public static void showAlertDialog(Context context,
                                       DialogInterface.OnClickListener positiveClick,
                                       DialogInterface.OnClickListener negativeClick,
                                       int message,
                                       int positiveBtnText,
                                       int negativeBtnText) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveBtnText, positiveClick)
                .setNegativeButton(negativeBtnText, negativeClick);
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }
}