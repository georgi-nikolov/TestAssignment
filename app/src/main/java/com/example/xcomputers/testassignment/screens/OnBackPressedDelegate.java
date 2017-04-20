package com.example.xcomputers.testassignment.screens;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Georgi on 4/19/2017.
 */

/**
 * Delegates the backpressed event to the fragment
 */
public class OnBackPressedDelegate {

    private int contentId;
    private FragmentManager manager;

    public OnBackPressedDelegate(FragmentManager manager, @IdRes int contentId) {

        this.contentId = contentId;
        this.manager = manager;
    }

    /**
     * Delegates the onBackPressed event from the Activity to the fragment inside
     * @return true if the fragment has consumed the event, false otherwise
     */
    public boolean onBackPressed() {

        Fragment fragment = manager.findFragmentById(contentId);
        return fragment != null &&
                fragment instanceof OnBackPressedListener &&
                ((OnBackPressedListener) fragment).onBackPressed();
    }
}
