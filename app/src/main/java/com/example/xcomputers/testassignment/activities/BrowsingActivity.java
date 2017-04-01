package com.example.xcomputers.testassignment.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.adapters.BrowsingAdapter;
import com.example.xcomputers.testassignment.R;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class BrowsingActivity extends BaseBrowsingActivity {

    private RecyclerView recyclerView;
    private TextView noItemsView;
    private BrowsingAdapter adapter;
    private Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);
        initActionBar(initToolBackBackButtonAction());
        noItemsView = (TextView) findViewById(R.id.no_files_TV);
        initRecycler();
        initCallBack();
        String accessToken = getIntent().getStringExtra(LoginActivity.ACCESS_TOKEN);
        initClient(accessToken);
        listFolder(RemoteFolder.ROOT_FOLDER_ID, callback);
    }

    private View.OnClickListener initToolBackBackButtonAction() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
                    listFolder(currentFolder.parentFolderId(), callback);
                }
            }
        };
    }

    private void initCallBack() {

        this.callback = new Callback<RemoteFolder>() {
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
                hideLoading();
            }

            @Override
            public void onFailure(Call<RemoteFolder> call, Throwable t) {
                hideLoading();
                Toast.makeText(BrowsingActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        List<RemoteEntry> entryList = new ArrayList<>();
        adapter = new BrowsingAdapter(entryList);
        adapter.setOnResultClickListener(createResultClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BrowsingActivity.this));
    }

    private void handleItemCount(int childCount, TextView noItemsView, RecyclerView recyclerView) {

        noItemsView.setVisibility(childCount == 0 ? View.VISIBLE : GONE);
        recyclerView.setVisibility(childCount == 0 ? GONE : View.VISIBLE);
    }

    private BrowsingAdapter.OnResultClickListener createResultClickListener() {

        return new BrowsingAdapter.OnResultClickListener() {
            @Override
            public void onResultClicked(View view, int position) {
                RemoteEntry entry = adapter.getData().get(position);
                if (entry.isFolder()) {
                    listFolder(entry.asFolder().folderId(), callback);
                } else {
                    RemoteFile remoteFile = entry.asFile();
                    File localFolder = getFilesDir();
                    downloadAndOpenFile(remoteFile, new File(localFolder, remoteFile.name()));
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
            listFolder(currentFolder.parentFolderId(), callback);
        } else {
            super.onBackPressed();
        }
    }
}
