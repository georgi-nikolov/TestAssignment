package com.example.xcomputers.testassignment.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.adapters.BrowsingAdapter;
import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.DownloadUtil;
import com.example.xcomputers.testassignment.util.ProgressUtil;
import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.PCloudSdk;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static android.view.View.GONE;

public class BrowsingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noItemsView;
    private BrowsingAdapter adapter;
    private Handler uiHandler;
    private ApiClient client;
    private RemoteFolder currentFolder;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        String accessToken = getIntent().getStringExtra(LoginActivity.ACCESS_TOKEN);
        initClient(accessToken);
        setActionBar();
        uiHandler = new Handler(getMainLooper());
        noItemsView = (TextView) findViewById(R.id.no_files_TV);
        initRecycler();
        makeCall(RemoteFolder.ROOT_FOLDER_ID);
    }


    private void initClient(String accessToken) {

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

    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        List<RemoteEntry> entryList = new ArrayList<>();
        adapter = new BrowsingAdapter(entryList, BrowsingActivity.this);
        adapter.setOnResultClickListener(createResultClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BrowsingActivity.this));
    }

    private void setActionBar() {

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(getString(R.string.root));
        //TODO figure out why the logo image is now showing...
        ImageButton backButton = (ImageButton) findViewById(R.id.back);
        backButton.setImageResource(R.drawable.ic_arrow_up);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFolder != null) {
                    makeCall(currentFolder.parentFolderId());
                }
            }
        });
    }

    private void makeCall(final long folderId) {

        ProgressUtil.showLoading(this);

        Call<RemoteFolder> call = client.listFolder(folderId);

        call.enqueue(new Callback<RemoteFolder>() {
            @Override
            public void onResponse(Call<RemoteFolder> call, RemoteFolder response) {
                int childCount = response.children().size();
                handleItemCount(childCount, noItemsView, recyclerView);
                if (childCount > 0) {
                    List<RemoteEntry> entries = response.children();
                    adapter.setData(entries);
                }
                currentFolder = response;
                setActionTitle(response.name());
                ProgressUtil.hideLoading();
            }

            @Override
            public void onFailure(Call<RemoteFolder> call, Throwable t) {
                ProgressUtil.hideLoading();
                Toast.makeText(BrowsingActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleItemCount(int childCount, TextView noItemsView, RecyclerView recyclerView) {

        noItemsView.setVisibility(childCount == 0 ? View.VISIBLE : GONE);
        recyclerView.setVisibility(childCount == 0 ? GONE : View.VISIBLE);
    }

    private void setActionTitle(String folderName) {

        if (folderName.equals(File.separator)) {
            folderName = getString(R.string.root);
        }
        titleTextView.setText(folderName);
    }

    private BrowsingAdapter.OnResultClickListener createResultClickListener() {

        return new BrowsingAdapter.OnResultClickListener() {
            @Override
            public void onResultClicked(View view, int position) {
                RemoteEntry entry = adapter.getData().get(position);
                if (entry.isFolder()) {
                    makeCall(entry.asFolder().folderId());
                } else {
                    RemoteFile remoteFile = entry.asFile();
                    File localFolder = getFilesDir();
                    DownloadUtil.download(remoteFile, new File(localFolder, remoteFile.name()), BrowsingActivity.this);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (currentFolder != null) {
            makeCall(currentFolder.parentFolderId());
        }
    }
}
