package com.example.xcomputers.testassignment.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Georgi on 4/19/2017.
 */

/**
 * A contract for an activity to open different fragments to represent screens
 */
public interface NavigationHandler {

    /**
     * Opens a fragment in the Activity
     * @param fragment  The fragment which you'd like opened
     * @param addToBackStack A boolean value to indicate whether to add to backstack or not
     * @param args  A bundle to attach to the fragment
     */
    void openView(Class<? extends Fragment> fragment, boolean addToBackStack, Bundle args);
}
