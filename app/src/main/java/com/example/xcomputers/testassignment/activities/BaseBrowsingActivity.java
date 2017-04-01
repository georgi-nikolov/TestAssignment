package com.example.xcomputers.testassignment.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.BuildConfig;
import com.example.xcomputers.testassignment.R;
import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.PCloudSdk;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;


/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * This class is a base implementation to house the basic logic for
 * the APIClient, the toolbar, the loading dialog and the file downloading logic
 */
public abstract class BaseBrowsingActivity extends AppCompatActivity {

    private Handler uiHandler;
    protected ApiClient client;
    private ProgressDialog loadingDialog;
    private TextView titleTextView;
    protected RemoteFolder currentFolder;
    private boolean doubleBackToExitPressedOnce;
    private static final int DEFAULT_DOUBLE_TAP_WAIT = 2000; //milliseconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHandler = new Handler(getMainLooper());
    }

    /**
     * Initializes the action bar and sets a click listener for the back button
     *
     * @param backButtonClickListener the listener to be fired upon pressing the back button
     */
    protected void initActionBar(@Nullable View.OnClickListener backButtonClickListener) {

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(getString(R.string.root));
        //TODO figure out why the logo image is now showing...
        ImageButton backButton = (ImageButton) findViewById(R.id.back);
        backButton.setImageResource(R.drawable.ic_arrow_up);
        backButton.setOnClickListener(backButtonClickListener);
    }

    /**
     * Changes the action bar title
     *
     * @param title A string to be set as the action bar tittle
     */
    protected void setActionTitle(@NonNull String title) {

        if (title.equals(File.separator)) {
            title = getString(R.string.root);
        }
        titleTextView.setText(title);
    }

    /**
     * Initializes the APIClient
     *
     * @param accessToken The accessToken received upon successful auth
     */
    protected void initClient(@NonNull String accessToken) {

        client = PCloudSdk.newClientBuilder()
                .authenticator(Authenticators.newOAuthAuthenticator(accessToken))
                .callbackExecutor(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable runnable) {
                        uiHandler.post(runnable);
                    }
                })
                .create();
    }

    /**
     * Calls the API and gets the contents of a folder
     *
     * @param folderId The ID of the folder you'd like listed
     * @param callback The callback to be fired for this request
     */
    protected void listFolder(final long folderId, @NonNull Callback callback) {

        showLoading();

        Call<RemoteFolder> call = client.listFolder(folderId);

        call.enqueue(callback);
    }

    /**
     * Shows a loading dialog on screen
     */
    protected void showLoading() {

        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setMessage(getString(R.string.loading_message));
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }


    /**
     * Hides the loading dialog from the screen
     */
    protected void hideLoading() {

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * Utility method for downloading a file and then opening it
     * if an app on the device can handle the file type
     *
     * @param fileToDownload The RemoteFile you'd like to download
     * @param localFile      A reference to the downloaded file on your device
     */
    protected void downloadAndOpenFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile) {

        new AsyncTask<RemoteFile, Void, File>() {

            @Override
            protected void onPreExecute() {
                showLoading();
                super.onPreExecute();
            }

            @Override
            protected File doInBackground(RemoteFile... remoteFiles) {
                try {
                    fileToDownload.download(DataSink.create(localFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BaseBrowsingActivity.this, R.string.download_error_message, Toast.LENGTH_SHORT).show();
                }
                return localFile;
            }

            @Override
            protected void onPostExecute(File file) {

                openFile(file, fileToDownload.contentType());
            }
        }.execute();
    }

    private void openFile(File file, String contentType){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(BaseBrowsingActivity.this, BuildConfig.APPLICATION_ID, file);
        intent.setDataAndType(uri, contentType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        hideLoading();
        PackageManager pm = getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(BaseBrowsingActivity.this, R.string.file_open_error_message, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.double_back_exit_message, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, DEFAULT_DOUBLE_TAP_WAIT);
    }
}

