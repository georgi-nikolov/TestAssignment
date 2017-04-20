package com.example.xcomputers.testassignment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.xcomputers.testassignment.MyApplication;
import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.TokenKeeper;
import com.example.xcomputers.testassignment.util.AlertDialogUtil;
import com.pcloud.sdk.AuthorizationActivity;
import com.pcloud.sdk.AuthorizationResult;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

    private static final int PCLOUD_AUTHORIZATION_REQUEST_CODE = 123;
    private static final String CLIENT_ID = "VY5nA56YTC7";
    public static final String ACCESS_TOKEN = "access_token";

    @Inject
    TokenKeeper keeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((MyApplication)getApplicationContext()).component().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
    }

    private void checkConnectivity(){

        if (isConnectingToTheInternet()) {
            initiateLogin();
        } else {
            promptInternetConnection(new AlertDialogUtil.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    checkConnectivity();
                }
            },new AlertDialogUtil.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.this.finish();
                }
            });
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
        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        keeper.setToken(accessToken);
        startActivity(loginIntent);
        finish();
    }

    private void requestCredentials() {

        Intent authIntent = AuthorizationActivity.createIntent(LoginActivity.this, CLIENT_ID);
        startActivityForResult(authIntent, PCLOUD_AUTHORIZATION_REQUEST_CODE);
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