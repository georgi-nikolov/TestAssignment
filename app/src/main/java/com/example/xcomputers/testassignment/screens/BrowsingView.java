package com.example.xcomputers.testassignment.screens;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.BuildConfig;
import com.example.xcomputers.testassignment.activities.IActivity;
import com.example.xcomputers.testassignment.adapters.BrowsingAdapter;
import com.example.xcomputers.testassignment.R;
import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class BrowsingView extends Fragment implements InsideView {

    private RecyclerView recyclerView;
    private TextView noItemsView;
    private BrowsingAdapter adapter;
    private RemoteFolder currentFolder;
    private ApiClient client;
    private IActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browsing_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        noItemsView = (TextView) view.findViewById(R.id.no_files_TV);
        initRecycler(view);
        activity.setToolbarBackAction(initToolBackBackButtonAction());
        client = activity.getClient();
        listFolder(RemoteFolder.ROOT_FOLDER_ID);
    }

    public void setActivity(IActivity activity) {

        this.activity = activity;
    }

    private void initRecycler(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        List<RemoteEntry> entryList = new ArrayList<>();
        adapter = new BrowsingAdapter(entryList);
        adapter.setOnResultClickListener(createResultClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private View.OnClickListener initToolBackBackButtonAction() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
                    listFolder(currentFolder.parentFolderId());
                }
            }
        };
    }

    private void listFolder(final long folderId) {

        activity.showLoading();

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
                activity.setToolbarTitle(response.name());
                activity.hideLoading();
            }

            @Override
            public void onFailure(Call<RemoteFolder> call, Throwable t) {
                activity.hideLoading();
                Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        });
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
                    listFolder(entry.asFolder().folderId());
                } else {
                    RemoteFile remoteFile = entry.asFile();
                    File localFolder = getContext().getFilesDir();
                    downloadAndOpenFile(remoteFile, new File(localFolder, remoteFile.name()));
                }
            }
        };
    }

    private void downloadAndOpenFile(@NonNull final RemoteFile fileToDownload, @NonNull final File localFile) {

        new AsyncTask<RemoteFile, Void, File>() {

            @Override
            protected void onPreExecute() {
                activity.showLoading();
                super.onPreExecute();
            }

            @Override
            protected File doInBackground(RemoteFile... remoteFiles) {
                try {
                    fileToDownload.download(DataSink.create(localFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.download_error_message, Toast.LENGTH_SHORT).show();
                }
                return localFile;
            }

            @Override
            protected void onPostExecute(File file) {

                openFile(file, fileToDownload.contentType());
            }
        }.execute();
    }

    private void openFile(File file, String contentType) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID, file);
        intent.setDataAndType(uri, contentType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.hideLoading();
        PackageManager pm = getContext().getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), R.string.file_open_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean hasHandledBackPress() {
        if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
            listFolder(currentFolder.parentFolderId());
            return true;
        }
        return false;
    }
}
