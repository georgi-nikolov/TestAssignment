package com.example.xcomputers.testassignment.dagger;

import com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter;
import com.example.xcomputers.testassignment.util.FileManager;
import com.pcloud.sdk.ApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Georgi on 4/20/2017.
 */
@Module
public class BrowsingModule {

    @Provides
    @Singleton
    FileManager provideManager(ApiClient client){

        return new FileManager(client);
    }

    @Provides
    @Singleton
    BrowsingPresenter providePresenter(FileManager manager){

        return new BrowsingPresenter(manager);
    }
}
