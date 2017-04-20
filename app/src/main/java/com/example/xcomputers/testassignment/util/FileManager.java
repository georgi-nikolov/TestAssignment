package com.example.xcomputers.testassignment.util;

import android.support.annotation.NonNull;

import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;


import okio.BufferedSource;
import rx.Observable;


/**
 * Created by Georgi on 4/19/2017.
 */

public class FileManager {

    private ApiClient client;

    public FileManager(ApiClient client) {

        this.client = client;
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
