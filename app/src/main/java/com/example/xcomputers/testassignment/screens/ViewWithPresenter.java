package com.example.xcomputers.testassignment.screens;

/**
 * Created by Georgi on 4/18/2017.
 */

public interface ViewWithPresenter<P extends Presenter> {

    P getPresenter();
}
