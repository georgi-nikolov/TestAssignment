package com.example.xcomputers.testassignment.util;

import android.support.annotation.NonNull;

import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;


import javax.inject.Inject;

import okio.BufferedSource;
import rx.Observable;
import rx.subscriptions.Subscriptions;

import static com.pcloud.sdk.internal.IOUtils.closeQuietly;


/**
 * Created by Georgi on 4/19/2017.
 */

/**
 * This class handles the API calls for getting the contents of a given folder and downloading a file
 */
public class FileManager {

    private ApiClient client;

    @Inject
    FileManager(ApiClient client) {

        this.client = client;
    }

    /**
     * Downloads a file from the cloud
     * @param fileToDownload The file you;d like to download
     * @param localFile The place where you'd like the file to be downloaded to
     * @return An RxJava Observable object with the downloaded File
     */
    public Observable<File> downloadFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile) {
        return Observable.<File>create(subscriber -> {
            Call<BufferedSource> downloadCall = client.download(fileToDownload);
            subscriber.add(Subscriptions.create(downloadCall::cancel));

            BufferedSource source = null;
            try {
                if (!subscriber.isUnsubscribed()) {
                    source = downloadCall.execute();
                }
                DataSink sink = DataSink.create(localFile);
                sink.readAll(source);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(localFile);
                    subscriber.onCompleted();
                }
            } catch (Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            } finally {
                closeQuietly(source);
            }
        }).<File>onBackpressureBuffer();
    }

    /**
     * Calls the API to get the contents of a given folder
     * @param folderId The Id of the folder you'd like to get the contents for
     * @return An RxJava Observable object with the RemoteFolder response
     */
    public Observable<RemoteFolder> listFolder(final long folderId) {

        return Observable.fromCallable(() -> {

            Call<RemoteFolder> call = client.listFolder(folderId);
            return call.execute();
        });
    }
}
