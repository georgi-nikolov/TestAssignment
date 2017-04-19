package com.example.xcomputers.testassignment.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.PCloudSdk;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okio.BufferedSource;
import rx.Observable;


/**
 * Created by Georgi on 4/19/2017.
 */

public class FileManager {

    private static final long DEFAULT_TIMEOUT_IN_MILIS = 15000;

    private ApiClient client;

    public FileManager(String accessToken) {

        initClient(accessToken, new Handler(Looper.getMainLooper()));
    }

    /**
     * Initializes the APIClient
     *
     * @param accessToken The accessToken received upon successful auth
     */
    private void initClient(@NonNull String accessToken, final Handler uiHandler) {

        client = PCloudSdk.newClientBuilder()
                .authenticator(Authenticators.newOAuthAuthenticator(accessToken))
                .connectTimeout(DEFAULT_TIMEOUT_IN_MILIS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT_IN_MILIS, TimeUnit.MILLISECONDS)
                .callbackExecutor(uiHandler::post)
                .create();
    }

    public Observable<File> downloadFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile) {

        return Observable.fromCallable(() -> {

            BufferedSource source = client.download(fileToDownload).execute();
            DataSink sink = DataSink.create(localFile);
            sink.readAll(source);
            return localFile;
        });
    }


    public Observable<RemoteFolder> listFolder(final long folderId) {

        return Observable.fromCallable(() -> {

            Call<RemoteFolder> call = client.listFolder(folderId);
            return call.execute();
        });
    }
}
