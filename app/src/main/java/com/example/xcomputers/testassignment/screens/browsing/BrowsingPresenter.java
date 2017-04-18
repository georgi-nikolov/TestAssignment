package com.example.xcomputers.testassignment.screens.browsing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.xcomputers.testassignment.BuildConfig;
import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.activities.IActivity;
import com.example.xcomputers.testassignment.screens.Presenter;
import com.example.xcomputers.testassignment.util.AlertDialogUtil;
import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.io.IOException;


/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * Not the pure form of Presenter in MVP
 * but more intended to house the networking logic to avoid clutter in the View
 */
public class BrowsingPresenter implements Presenter {

    private ApiClient client;
    private IActivity activity;

    void setActivity(IActivity activity) {

        this.activity = activity;
        this.client = activity.getClient();
    }

    /**
     * Downloads a file and attempts to open it
     *
     * @param fileToDownload The RemoteFile the user selected
     * @param localFile      The local file reference when donwloaded
     * @param context        The app context for displaying error messages
     */
    void downloadAndOpenFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile, @NonNull final Context context) {

        //TODO figure out how to avoid repeating this using only Java 7 functionality
        if (!activity.hasInternetConnectivity()) {
            activity.promptUserToConnect(new AlertDialogUtil.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                    downloadAndOpenFile(fileToDownload, localFile, context);
                }
            }, new AlertDialogUtil.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        } else {
            downloadFile(fileToDownload, localFile, context);
        }
    }

    private void downloadFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile, @NonNull final Context context) {

        new AsyncTask<RemoteFile, Void, File>() {

            @Override
            protected void onPreExecute() {
                activity.showLoading();
                super.onPreExecute();
            }

            @Override
            protected File doInBackground(RemoteFile... remoteFiles) {
                try {
                    fileToDownload.download(DataSink.create(localFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return localFile;
            }

            @Override
            protected void onPostExecute(File file) {
                if(file != null){
                    openFile(file, fileToDownload.contentType(), context);
                }else{
                    Toast.makeText(context, R.string.download_error_message, Toast.LENGTH_SHORT).show();
                    activity.hideLoading();
                }
            }
        }.execute();
    }

    private void openFile(File file, String contentType, Context context) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        intent.setDataAndType(uri, contentType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.hideLoading();
        PackageManager pm = context.getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.file_open_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Calls the API and fires a callback with the contents of a given folder
     *
     * @param folderId The folder to be called for
     * @param callback The callback to be fired with the response
     */
    void listFolder(final long folderId, final Callback callback) {

        if (!activity.hasInternetConnectivity()) {
            activity.promptUserToConnect(new AlertDialogUtil.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                    listFolder(folderId, callback);
                }
            }, new AlertDialogUtil.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        } else {
            callListFolder(folderId, callback);
        }
    }

    private void callListFolder(final long folderId, final Callback callback) {

        activity.showLoading();
        Call<RemoteFolder> call = client.listFolder(folderId);
        call.enqueue(callback);
    }
}
