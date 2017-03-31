package com.example.xcomputers.testassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        Intent authIntent = AuthorizationActivity.createIntent(LoginActivity.this, CLIENT_ID);
        startActivityForResult(authIntent, PCLOUD_AUTHORIZATION_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PCLOUD_AUTHORIZATION_REQUEST_CODE) {
            AuthorizationResult result = (AuthorizationResult) data.getSerializableExtra(AuthorizationActivity.KEY_AUTHORIZATION_RESULT);

            if (result == AuthorizationResult.ACCESS_GRANTED) {
                String accessToken = data.getStringExtra(AuthorizationActivity.KEY_ACCESS_TOKEN);
                long userId = data.getLongExtra(AuthorizationActivity.KEY_USER_ID, 0);
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                preferences.edit().putString(ACCESS_TOKEN, accessToken).apply();
                Intent silenLoginIntent = new Intent(LoginActivity.this, BrowsingActivity.class);
                silenLoginIntent.putExtra(ACCESS_TOKEN, accessToken);
                startActivity(silenLoginIntent);
            } else {
                //TODO handle error properly
            }
        }
    }
}