package com.example.xcomputers.testassignment.screens.browsing;

import android.support.annotation.NonNull;

import com.example.xcomputers.testassignment.util.FileManager;
import com.pcloud.sdk.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

import static com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter.Error.IO;
import static com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter.Error.NO_INTERNET;
import static com.example.xcomputers.testassignment.screens.browsing.BrowsingPresenter.Error.SOCKET_TIMEOUT;


/**
 * Created by xComputers on 01/04/2017.
 */


public class BrowsingPresenter extends com.neykov.mvp.RxPresenter<FolderDisplayView> {

    private FileManager manager;

    public enum Error {NO_INTERNET, SOCKET_TIMEOUT, IO}

    public void setManager(FileManager manager) {

        this.manager = manager;
    }

    public void listFolder(final long folderId) {
        //TODO return this logic when the SDK is fixed
        //doWhenViewBound(folderDisplayView -> folderDisplayView.setLoadingState(true));
        if(getView() != null){
            getView().setLoadingState(true);
        }

        this.add(manager.listFolder(folderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(deliver())
                .subscribe(delivery -> delivery.split(

                        (browsingView, folder) -> {
                            browsingView.displayFolder(folder);
                            browsingView.setLoadingState(false);
                        }, (browsingView, error) -> {
                            browsingView.displayError(getErrorMessage(error));
                            browsingView.setLoadingState(false);
                        }
                ))
        );
    }

    public void downloadFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile) {
        //TODO return this logic when the SDK is fixed
        //doWhenViewBound(folderDisplayView -> folderDisplayView.setLoadingState(true));

        if(getView() != null){
            getView().setLoadingState(true);
        }

        this.add(manager.downloadFile(fileToDownload, localFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(deliver())
                .subscribe(delivery -> delivery.split(
                        (browsingView, file) -> {
                            browsingView.openFile(file, fileToDownload.contentType());
                            browsingView.setLoadingState(false);
                        },
                        (browsingView, error) -> {
                            browsingView.displayError(getErrorMessage(error));
                            browsingView.setLoadingState(false);
                        }
                )));
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
