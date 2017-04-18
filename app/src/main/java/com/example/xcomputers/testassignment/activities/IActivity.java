package com.example.xcomputers.testassignment.activities;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.xcomputers.testassignment.util.AlertDialogUtil;
import com.pcloud.sdk.ApiClient;

/**
 * Created by xComputers on 01/04/2017.
 */

public interface IActivity {

    /**
     * Shows a loading dialog on screen
     */
    void showLoading();

    /**
     * Hides the loading dialog from the screen
     */
    void hideLoading();

    /**
     * Provides the api client instance
     *
     * @return An ApiClient instance
     */
    ApiClient getClient();

    /**
     * Opens a fragment in this activity
     *
     * @param fragment The fragment to open
     * @param addToBackStack true if you'd like the fragment added to the stack, false otherwise
     */
    void openView(Class<? extends Fragment> fragment, boolean addToBackStack);

    /**
     * Changes the action bar title
     *
     * @param title A string to be set as the action bar tittle
     */
    void setToolbarTitle(String title);

    /**
     * Sets the ToolBar back button behaviour
     *
     * @param listener The listener to be fired upon back press
     */
    void setToolbarBackAction(View.OnClickListener listener);

    /**
     * Display an AlertDialog to the user prompting him to enable an Internet connection
     *
     * @param positiveClick The action to be taken when the positive button is clicked
     * @param negativeClick The action to be taken when the negative button is clicked
     */
    void promptUserToConnect(AlertDialogUtil.OnClickListener positiveClick, AlertDialogUtil.OnClickListener negativeClick);

    /**
     * Checks if the device is currently connected to the Internet via WIFI or a mobile network
     *
     * @return true if the device is connected to the Internet, false otherwise
     */
    boolean hasInternetConnectivity();
}
