package com.example.xcomputers.testassignment.screens;

/**
 * Created by Georgi on 4/19/2017.
 */

public interface OnBackPressedListener {

    /**
     * Delegates the backPressed event to the insideView
     *
     * @return true if the insideView has consumed the event, false if the container should handle it
     */
    boolean onBackPressed();
}
