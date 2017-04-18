package com.example.xcomputers.testassignment.screens;

import android.support.v4.app.Fragment;

/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * An abstraction designed to assign a helper class to each view
 * This helper class should handle network operations to avoid cluttering the view class
 *
 * @param <P> The helper class to be assigned to the View
 */
public abstract class BaseView<P extends Presenter> extends Fragment {

    private P presenter;

    /**
     * Checks if the presenter exists and attempts to instantiate it if not
     *
     * @return an instance of the presenter
     * @throws IllegalArgumentException if the given presenter is null or if it can't be instantiated
     */
    public P getPresenter() {

        if (isSafe() && presenter == null) {
            presenter = createPresenter();
            if (presenter == null) {
                throw new IllegalStateException("The presenter cannot be null");
            }
        }
        return presenter;
    }

    /**
     * Creates an instance of the helper class
     *
     * @return You should return a new instance of the class you specified as a generic
     */
    protected abstract P createPresenter();

    /**
     * Checks if it is safe to operate with this fragment
     *
     * @return true if it is safe to operate with this fragment, false otherwise
     */
    protected boolean isSafe() {
        return !(this.isRemoving() || this.getActivity() == null || this.isDetached() || !this.isAdded() || this.getView() == null);
    }
}
