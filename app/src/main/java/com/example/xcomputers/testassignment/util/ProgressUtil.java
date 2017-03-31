package com.example.xcomputers.testassignment.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by xComputers on 31/03/2017.
 */

public class ProgressUtil {

    private static ProgressDialog loadingDialog;

    public static void showLoading(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(context);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setMessage("Loading. Please wait...");
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();

    }


    public static void hideLoading() {

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


}