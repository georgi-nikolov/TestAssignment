package com.example.xcomputers.testassignment;

import android.app.Application;

import com.example.xcomputers.testassignment.dagger.*;


/**
 * Created by Georgi on 4/20/2017.
 */

/**
 * An implementation for the Application class to house the Dagger component initialization
 */
public class BrowsingApplication extends Application {

    private AppComponent component;

    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent component() {
        return component;
    }

}
