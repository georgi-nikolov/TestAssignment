package com.example.xcomputers.testassignment.screens;

/**
 * Created by Georgi on 4/19/2017.
 */

public interface OnBackPressedListener {

    /**
     * Delegates the backPressed event to the Fragment inside the Activity
     *
     * @return true if the Fragment has consumed the event, false if the container Activity should handle it
     */
    boolean onBackPressed();
}
