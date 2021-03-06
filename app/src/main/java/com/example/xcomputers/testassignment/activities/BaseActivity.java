package com.example.xcomputers.testassignment.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.screens.OnBackPressedDelegate;
import com.example.xcomputers.testassignment.util.AlertDialogUtil;

/**
 * Created by xComputers on 02/04/2017.
 */

/**
 * An abstraction for an Activity to check the current internet connectivity and prompt the user to turn on WIFI or Mobile data
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected OnBackPressedDelegate onBackPressedDelegate;

    protected boolean isConnectingToTheInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnectedOrConnecting())
                return true;
        }
        return false;
    }

    protected void promptInternetConnection(AlertDialogUtil.OnClickListener positiveClick, AlertDialogUtil.OnClickListener negativeClick) {

        AlertDialogUtil util = AlertDialogUtil.newInstance(positiveClick, negativeClick, R.string.internet_prompt_message,
                R.string.internet_dialog_positive_message,
                R.string.internet_dialog_negative_mesasge);
        util.show(getSupportFragmentManager(), AlertDialogUtil.class.getName());
        util.setCancelable(false);
    }
}
