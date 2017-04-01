package com.example.xcomputers.testassignment.activities;

import android.support.v4.app.Fragment;
import android.view.View;

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
     */
    void openView(Class<? extends Fragment> fragment, boolean addToBackStack);

    /**
     * Changes the action bar title
     *
     * @param title A string to be set as the action bar tittle
     */
    void setToolbarTitle(String title);

    void setToolbarBackAction(View.OnClickListener listener);
}
