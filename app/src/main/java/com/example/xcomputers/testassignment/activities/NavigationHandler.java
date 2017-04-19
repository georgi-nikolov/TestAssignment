package com.example.xcomputers.testassignment.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Georgi on 4/19/2017.
 */

public interface NavigationHandler {

    void openView(Class<? extends Fragment> fragment, boolean addToBackStack, Bundle args);
}
