package com.example.xcomputers.testassignment.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.AlertDialogUtil;
import com.pcloud.sdk.AuthorizationActivity;
import com.pcloud.sdk.AuthorizationResult;

public class LoginActivity extends AppCompatActivity {

    private static final int PCLOUD_AUTHORIZATION_REQUEST_CODE = 123;
    private static final String CLIENT_ID = "VY5nA56YTC7";
    public static final String ACCESS_TOKEN = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnectingToTheInternet()) {
            initiateLogin();
        } else {
            promptInternetConnection();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PCLOUD_AUTHORIZATION_REQUEST_CODE) {
            AuthorizationResult result = (AuthorizationResult) data.getSerializableExtra(AuthorizationActivity.KEY_AUTHORIZATION_RESULT);

            if (result == AuthorizationResult.ACCESS_GRANTED) {
                String accessToken = data.getStringExtra(AuthorizationActivity.KEY_ACCESS_TOKEN);
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                prefs.edit().putString(ACCESS_TOKEN, accessToken).apply();
                login(accessToken);
            } else {
                requestCredentials();
            }
        }
    }

    private void login(String accessToken) {
        Intent loginIntent = new Intent(LoginActivity.this, BrowsingActivity.class);
        loginIntent.putExtra(ACCESS_TOKEN, accessToken);
        startActivity(loginIntent);
        finish();
    }

    private void requestCredentials() {

        Intent authIntent = AuthorizationActivity.createIntent(LoginActivity.this, CLIENT_ID);
        startActivityForResult(authIntent, PCLOUD_AUTHORIZATION_REQUEST_CODE);
    }

    private boolean isConnectingToTheInternet() {

        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnectedOrConnecting())
                return true;
        }
        return false;
    }

    private void promptInternetConnection() {

        AlertDialogUtil.showAlertDialog(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.this.finish();
                    }
                }, R.string.internet_prompt_message,
                R.string.internet_dialog_positive_message,
                R.string.internet_dialog_negative_mesasge);
    }

    private void initiateLogin() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if (preferences.contains(ACCESS_TOKEN)) {
            login(preferences.getString(ACCESS_TOKEN, ""));
        } else {
            requestCredentials();
        }
    }
}