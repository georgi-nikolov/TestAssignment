package com.example.xcomputers.testassignment.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.RemoteFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by xComputers on 31/03/2017.
 */

public class DownloadUtil {

    public static void download(final RemoteFile fileToDownload, final File localFile, final Context context) {

        new AsyncTask<RemoteFile, Void, File>() {
            @Override
            protected File doInBackground(RemoteFile... remoteFiles) {
                try {
                    fileToDownload.download(DataSink.create(localFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return localFile;
            }

            @Override
            protected void onPostExecute(File file) {
                //TODO do this properly with a content provider
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), fileToDownload.contentType());
                context.startActivity(intent);
                super.onPostExecute(file);
            }
        };
    }
}
