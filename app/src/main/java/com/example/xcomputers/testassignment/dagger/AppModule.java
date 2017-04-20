package com.example.xcomputers.testassignment.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xcomputers.testassignment.BrowsingApplication;
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

/**
 * The dagger module to provide the ApiClient, TokenKeeper and the ApplicationContext
 */
@Module
public class AppModule {

    private static final long DEFAULT_TIMEOUT_IN_MILLIS = 15000;

    private final BrowsingApplication application;

    public AppModule(BrowsingApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(TokenKeeper keeper) {

        return PCloudSdk.newClientBuilder()
                .authenticator(Authenticators.newOauthAuthenticator(keeper::getToken))
                .connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
                .callbackExecutor(new Handler(Looper.getMainLooper())::post)
                .create();
    }

    @Provides
    @Singleton
    TokenKeeper provideKeeper() {

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
