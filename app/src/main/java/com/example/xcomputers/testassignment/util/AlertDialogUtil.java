package com.example.xcomputers.testassignment.util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * Utility class for providing an alert dialog
 */
public class AlertDialogUtil extends DialogFragment{

    private static final String MESSAGE_KEY = "message";
    private static final String POSITIVE_BTN_TEXT_KEY = "positiveBtnText";
    private static final String NEGATIVE_BTN_TEXT_KEY = "negativeBtnText";
    private static final String POSITIVE_BTN_ACTION_KEY = "positiveBtnAction";
    private static final String NEGATIVE_BTN_ACTION_KEY = "negativeBtnAction";

    /**
     * Utility method that creates an alert dialog fragment
     *
     * @param positiveClick   onClick implementation for the positive button
     * @param negativeClick   onClick implementation for the negative button
     * @param message         the message to be set to the dialog
     * @param positiveBtnText the text for the positive button
     * @param negativeBtnText the text for the negative button
     *
     * @return An instance of a DialogFragment
     */
    public static AlertDialogUtil newInstance(OnClickListener positiveClick,
                                              OnClickListener negativeClick,
                                              int message,
                                              int positiveBtnText,
                                              int negativeBtnText) {
        AlertDialogUtil frag = new AlertDialogUtil();
        Bundle args = new Bundle();
        args.putInt(MESSAGE_KEY, message);
        args.putInt(POSITIVE_BTN_TEXT_KEY, positiveBtnText);
        args.putInt(NEGATIVE_BTN_TEXT_KEY, negativeBtnText);
        args.putParcelable(POSITIVE_BTN_ACTION_KEY, positiveClick);
        args.putParcelable(NEGATIVE_BTN_ACTION_KEY, negativeClick);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setMessage(args.getInt(MESSAGE_KEY))
                .setCancelable(false)
                .setPositiveButton(args.getInt(POSITIVE_BTN_TEXT_KEY), args.getParcelable(POSITIVE_BTN_ACTION_KEY))
                .setNegativeButton(args.getInt(NEGATIVE_BTN_TEXT_KEY), args.getParcelable(NEGATIVE_BTN_ACTION_KEY));
        return builder.create();
    }

    /**
     * A parcelable implementation of an DialogInterface.OnClickListener to be used in a Bundle
     */
    public abstract static class OnClickListener implements DialogInterface.OnClickListener, Parcelable {


        @Override
        public abstract void onClick(DialogInterface dialog, int which);

        @Override
        public void writeToParcel(Parcel dest, int flags) {}

        @Override
        public int describeContents() {
            return 0;
        }
    }
}