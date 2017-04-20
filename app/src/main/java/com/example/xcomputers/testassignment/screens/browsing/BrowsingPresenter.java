package com.example.xcomputers.testassignment.screens.browsing;

import android.support.annotation.NonNull;

import com.example.xcomputers.testassignment.util.FileManager;
import com.pcloud.sdk.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter.Error.IO;
import static com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter.Error.NO_INTERNET;
import static com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter.Error.SOCKET_TIMEOUT;


/**
 * Created by xComputers on 01/04/2017.
 */

public class BrowsingPresenter extends com.neykov.mvp.RxPresenter<FolderDisplayView> {

    private FileManager manager;
    private Subscription listFolderSubscription;
    private Subscription downloadFileSubscription;

    @Inject
    BrowsingPresenter(FileManager manager) {

        this.manager = manager;
    }

    enum Error {

        /**
         * The device is not connected ot the internet
         */
        NO_INTERNET,

        /**
         * The connection timed out
         */
        SOCKET_TIMEOUT,

        /**
         * Any kind of IO exception
         */
        IO
    }

    void listFolder(final long folderId) {

        if (listFolderSubscription != null &&
                !listFolderSubscription.isUnsubscribed()) {
            return;
        }

        doWhenViewBound(folderDisplayView -> folderDisplayView.setLoadingState(true));

        listFolderSubscription = manager.listFolder(folderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(deliver())
                .doOnTerminate(() -> listFolderSubscription = null)
                .subscribe(delivery -> delivery.split(

                        (browsingView, folder) -> {
                            browsingView.displayFolder(folder);
                            browsingView.setLoadingState(false);
                        }, (browsingView, error) -> {
                            browsingView.displayError(getErrorMessage(error));
                            browsingView.setLoadingState(false);
                        }
                ));

        this.add(listFolderSubscription);
    }

    void downloadFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile) {

        if (downloadFileSubscription != null &&
                !downloadFileSubscription.isUnsubscribed()){
            return;
        }

        doWhenViewBound(folderDisplayView -> folderDisplayView.setLoadingState(true));

        downloadFileSubscription = manager.downloadFile(fileToDownload, localFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(deliver())
                .doOnTerminate(() -> downloadFileSubscription = null)
                .subscribe(delivery -> delivery.split(
                        (browsingView, file) -> {
                            browsingView.openFile(file, fileToDownload.contentType());
                            browsingView.setLoadingState(false);
                        },
                        (browsingView, error) -> {
                            browsingView.displayError(getErrorMessage(error));
                            browsingView.setLoadingState(false);
                        }
                ));

        this.add(downloadFileSubscription);
    }

    private Error getErrorMessage(Throwable throwable) {

        Error error = null;

        if (throwable instanceof UnknownHostException) {
            error = NO_INTERNET;
        } else if (throwable instanceof SocketTimeoutException) {
            error = SOCKET_TIMEOUT;
        } else if (throwable instanceof IOException) {
            error = IO;
        }
        return error;
    }
}
