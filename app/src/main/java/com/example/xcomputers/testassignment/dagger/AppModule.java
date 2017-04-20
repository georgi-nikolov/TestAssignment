package com.example.xcomputers.testassignment.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xcomputers.testassignment.MyApplication;
import com.example.xcomputers.testassignment.util.TokenKeeper;
import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.PCloudSdk;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Georgi on 4/20/2017.
 */
@Module
public class AppModule {

    private static final long DEFAULT_TIMEOUT_IN_MILIS = 15000;

    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(TokenKeeper keeper){

        return PCloudSdk.newClientBuilder()
                .authenticator(Authenticators.newOauthAuthenticator(keeper::getToken))
                .connectTimeout(DEFAULT_TIMEOUT_IN_MILIS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT_IN_MILIS, TimeUnit.MILLISECONDS)
                .callbackExecutor(new Handler(Looper.getMainLooper())::post)
                .create();
    }

    @Provides
    @Singleton
    TokenKeeper provideKeeper(){

        return new TokenKeeper();
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link PerApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @PerApplication
    Context provideApplicationContext() {
        return application;
    }

}
