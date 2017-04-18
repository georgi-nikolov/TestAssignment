package com.example.xcomputers.testassignment.util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;


/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * Utility class for providing an alert dialog
 */
public class AlertDialogUtil extends DialogFragment{

    /**
     * Utility method that displays an alert dialog
     *
     * @param positiveClick   onClick implementation for the positive button
     * @param negativeClick   onClick implementation for the negative button
     * @param message         the message to be set to the dialog
     * @param positiveBtnText the text for the positive button
     * @param negativeBtnText the text for the negative button
     */
    public static AlertDialogUtil newInstance(OnClickListener positiveClick,
                                              OnClickListener negativeClick,
                                              int message,
                                              int positiveBtnText,
                                              int negativeBtnText) {
        AlertDialogUtil frag = new AlertDialogUtil();
        Bundle args = new Bundle();
        args.putInt("message", message);
        args.putInt("positiveBtnText", positiveBtnText);
        args.putInt("negativeBtnText", negativeBtnText);
        args.putParcelable("positiveClick", positiveClick);
        args.putParcelable("negativeClick", negativeClick);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setMessage(args.getInt("message"))
                .setCancelable(false)
                .setPositiveButton(args.getInt("positiveBtnText"), (DialogInterface.OnClickListener) args.getParcelable("positiveClick"))
                .setNegativeButton(args.getInt("negativeBtnText"), (DialogInterface.OnClickListener) args.getParcelable("negativeClick"));
        return builder.create();
    }

    public abstract static class OnClickListener implements DialogInterface.OnClickListener, Parcelable {


        @Override
        public abstract void onClick(DialogInterface dialog, int which);

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

    }
}