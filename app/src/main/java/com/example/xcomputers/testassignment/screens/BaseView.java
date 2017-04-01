package com.example.xcomputers.testassignment.screens;

import android.support.v4.app.Fragment;

/**
 * Created by xComputers on 01/04/2017.
 */

public abstract class BaseView<P> extends Fragment {

    protected P presenter;

    public BaseView() {

        presenter = createPresenter();
    }

    protected abstract P createPresenter();
}
