package com.example.xcomputers.testassignment.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.AlertDialogUtil;

/**
 * Created by xComputers on 02/04/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isConnectingToTheInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnectedOrConnecting())
                return true;
        }
        return false;
    }

    protected void promptInternetConnection(AlertDialogUtil.OnClickListener positiveClick, AlertDialogUtil.OnClickListener negativeClick) {

       AlertDialogUtil util =  AlertDialogUtil.newInstance(positiveClick, negativeClick, R.string.internet_prompt_message,
                R.string.internet_dialog_positive_message,
                R.string.internet_dialog_negative_mesasge);
        util.show(getFragmentManager(), "some tag");
        util.setCancelable(false);
    }
}
