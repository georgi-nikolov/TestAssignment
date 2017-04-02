package com.example.xcomputers.testassignment.screens;

import android.support.v4.app.Fragment;

/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * An abstraction designed to assign a helper class to each view
 * This helper class should handle network operations to avoid cluttering the view class
 * @param <P>   The helper class to be assigned to the View
 */
public abstract class BaseView<P> extends Fragment {

    protected P presenter;

    public BaseView() {

        presenter = createPresenter();
    }

    /**
     * Creates an instance of the helper class
     * @return  You should return a new instance of the class you specified as a generic
     */
    protected abstract P createPresenter();
}
