package com.example.xcomputers.testassignment;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.PCloudSdk;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFolder;

import java.util.List;

import static com.example.xcomputers.testassignment.LoginActivity.ACCESS_TOKEN;

public class BrowsingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        String accessToken = getIntent().getStringExtra(ACCESS_TOKEN);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        ApiClient apiClient = PCloudSdk.newClientBuilder()
                .authenticator(Authenticators.newOAuthAuthenticator(accessToken))
                .create();

        Call<RemoteFolder> call = apiClient.listFolder(RemoteFolder.ROOT_FOLDER_ID);

        call.enqueue(new Callback<RemoteFolder>() {
            @Override
            public void onResponse(Call<RemoteFolder> call, final RemoteFolder response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<RemoteEntry> entryList = response.children();
                        BrowsingAdapter adapter = new BrowsingAdapter(entryList, BrowsingActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BrowsingActivity.this));
                    }
                });
            }

            @Override
            public void onFailure(Call<RemoteFolder> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }
}
