package com.example.xcomputers.testassignment.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.screens.BrowsingView;
import com.example.xcomputers.testassignment.screens.InsideView;
import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.PCloudSdk;

import java.io.File;
import java.util.concurrent.Executor;


/**
 * Created by xComputers on 01/04/2017.
 */

/**
 * This class is a base implementation to house the basic logic for
 * the APIClient and the toolbar as well as navigation logic
 */
public class BaseActivity extends AppCompatActivity implements IActivity {

    private static final int DEFAULT_DOUBLE_TAP_WAIT = 2000; //milliseconds

    private Handler uiHandler;
    private ApiClient client;
    private ProgressDialog loadingDialog;
    private TextView titleTextView;
    private ImageButton backButton;
    private boolean doubleBackToExitPressedOnce;
    private InsideView insideView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);
        uiHandler = new Handler(getMainLooper());
        String accessToken = getIntent().getStringExtra(LoginActivity.ACCESS_TOKEN);
        initClient(accessToken);
        backButton = (ImageButton) findViewById(R.id.back);
        initActionBar();
        openView(BrowsingView.class, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initActionBar() {

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(getString(R.string.root));
        //TODO figure out why the logo image is now showing...
        backButton.setImageResource(R.drawable.ic_arrow_up);
    }

    @Override
    public void setToolbarBackAction(View.OnClickListener listener) {

        backButton.setOnClickListener(listener);
    }

    @Override
    public void setToolbarTitle(@NonNull String title) {

        if (title.equals(File.separator)) {
            title = getString(R.string.root);
        }
        titleTextView.setText(title);
    }

    @Override
    public ApiClient getClient() {

        return client;
    }

    @Override
    public void openView(Class<? extends Fragment> fragment, boolean addToBackStack) {

        if (fragment == null) {
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftx = fm.beginTransaction();

        String fragmentName = fragment.getName();
        Fragment fragmentToOpen = Fragment.instantiate(this, fragmentName);
        insideView = (InsideView) fragmentToOpen;
        insideView.setActivity(this);

        ftx.replace(R.id.container, fragmentToOpen, fragmentName);
        String backStackTag = addToBackStack ? fragmentName : null;
        ftx.addToBackStack(backStackTag);

        ftx.commit();
        fm.executePendingTransactions();
    }

    private void popBackStack() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() <= 1) {
            System.exit(0);
        } else {
            fm.popBackStackImmediate();
        }
    }

    /**
     * Initializes the APIClient
     *
     * @param accessToken The accessToken received upon successful auth
     */
    private void initClient(@NonNull String accessToken) {

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

    @Override
    public void showLoading() {

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

    @Override
    public void hideLoading() {

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        if (insideView.hasHandledBackPress()) {
            return;
        }

        if (doubleBackToExitPressedOnce) {
            popBackStack();
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

