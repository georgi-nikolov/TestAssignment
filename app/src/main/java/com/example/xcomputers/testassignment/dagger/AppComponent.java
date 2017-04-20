package com.example.xcomputers.testassignment.dagger;

import com.example.xcomputers.testassignment.activities.LoginActivity;
import com.example.xcomputers.testassignment.screens.browsing.BrowsingViewFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Georgi on 4/20/2017.
 */

/**
 * The main dagger component
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(BrowsingViewFragment browsingViewFragment);
}

