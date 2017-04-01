package com.example.xcomputers.testassignment.screens;

import com.example.xcomputers.testassignment.activities.IActivity;

/**
 * Created by xComputers on 01/04/2017.
 */

public interface InsideView {

    /**
     * Delegates the backPressed event to the insideView
     *
     * @return true if the insideView has consumed the event, false if the container should handle it
     */
    boolean hasHandledBackPress();

    /**
     * Sets the container activity for this insideView
     *
     * @param activity The container activity to house this insideView
     */
    void setActivity(IActivity activity);
}
