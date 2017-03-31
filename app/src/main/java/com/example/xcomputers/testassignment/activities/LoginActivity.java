package com.example.xcomputers.testassignment.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.ProgressUtil;
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
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if (preferences.contains(ACCESS_TOKEN)) {
            login(preferences.getString(ACCESS_TOKEN, ""));
        } else {
            requestCredentials();
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
    }

    private void requestCredentials() {

        Intent authIntent = AuthorizationActivity.createIntent(LoginActivity.this, CLIENT_ID);
        startActivityForResult(authIntent, PCLOUD_AUTHORIZATION_REQUEST_CODE);
    }
}