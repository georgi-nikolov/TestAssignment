package com.example.xcomputers.testassignment.screens.browsing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.activities.IActivity;
import com.example.xcomputers.testassignment.adapters.BrowsingAdapter;
import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.screens.BaseView;
import com.example.xcomputers.testassignment.screens.InsideView;
import com.example.xcomputers.testassignment.screens.Presenter;
import com.pcloud.sdk.Call;
import com.pcloud.sdk.Callback;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class BrowsingView extends BaseView<BrowsingPresenter> implements InsideView {

    private RecyclerView recyclerView;
    private TextView noItemsView;
    private BrowsingAdapter adapter;
    private RemoteFolder currentFolder;
    private IActivity activity;
    private Callback callback;
    private BrowsingPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browsing_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter = getPresenter();
        noItemsView = (TextView) view.findViewById(R.id.no_files_TV);
        initRecycler(view);
        activity.setToolbarBackAction(initToolBackBackButtonAction());
        presenter.setActivity(activity);
        callback = createCallback();
        presenter.listFolder(RemoteFolder.ROOT_FOLDER_ID, callback);
    }

    @Override
    public void setActivity(IActivity activity) {

        this.activity = activity;
    }

    private void initRecycler(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        List<RemoteEntry> entryList = new ArrayList<>();
        adapter = new BrowsingAdapter(entryList);
        adapter.setOnResultClickListener(createAdapterListener());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private BrowsingAdapter.OnResultClickListener createAdapterListener() {

        return new BrowsingAdapter.OnResultClickListener() {
            @Override
            public void onResultClicked(View view, int position) {
                RemoteEntry entry = adapter.getData().get(position);
                if (entry.isFolder()) {
                    presenter.listFolder(entry.asFolder().folderId(), callback);
                } else {
                    RemoteFile remoteFile = entry.asFile();
                    File localFolder = getContext().getFilesDir();
                    presenter.downloadAndOpenFile(remoteFile, new File(localFolder, remoteFile.name()), getContext());
                }
            }
        };
    }

    private View.OnClickListener initToolBackBackButtonAction() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
                    presenter.listFolder(currentFolder.parentFolderId(), callback);
                }
            }
        };
    }

    private Callback createCallback() {

        return new Callback<RemoteFolder>() {
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
        };
    }

    private void handleItemCount(int childCount, TextView noItemsView, RecyclerView recyclerView) {

        noItemsView.setVisibility(childCount == 0 ? View.VISIBLE : GONE);
        recyclerView.setVisibility(childCount == 0 ? GONE : View.VISIBLE);
    }

    @Override
    public boolean hasHandledBackPress() {
        if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
            presenter.listFolder(currentFolder.parentFolderId(), callback);
            return true;
        }
        return false;
    }

    @Override
    protected BrowsingPresenter createPresenter() {
        return new BrowsingPresenter();
    }
}
