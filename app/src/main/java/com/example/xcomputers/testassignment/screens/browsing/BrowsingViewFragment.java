package com.example.xcomputers.testassignment.screens.browsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xcomputers.testassignment.BuildConfig;
import com.example.xcomputers.testassignment.activities.MainActivity;
import com.example.xcomputers.testassignment.activities.NavigationHandler;
import com.example.xcomputers.testassignment.adapters.BrowsingAdapter;
import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.screens.OnBackPressedListener;
import com.neykov.mvp.support.ViewFragment;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class BrowsingViewFragment extends ViewFragment<BrowsingPresenter> implements OnBackPressedListener, FolderDisplayView {

    private RecyclerView recyclerView;
    private TextView noItemsView;
    private BrowsingAdapter adapter;
    private RemoteFolder currentFolder;
    private BrowsingPresenter presenter;
    private ProgressDialog loadingDialog;
    private TextView titleTextView;
    private ImageButton backButton;
    private NavigationHandler navigator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.browsing_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter = getPresenter();
        presenter.setManager(((MainActivity) getActivity()).manager);
        noItemsView = (TextView) view.findViewById(R.id.no_files_TV);
        initRecycler(view);
        presenter.listFolder(RemoteFolder.ROOT_FOLDER_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            initActionBar((AppCompatActivity) context);
            setToolbarBackAction(initToolBackBackButtonAction());
        }
        if (context instanceof NavigationHandler) {
            this.navigator = (NavigationHandler) context;
        }
    }

    private void initActionBar(AppCompatActivity context) {

        Toolbar actionBar = (Toolbar) context.findViewById(R.id.actionBar);
        context.setSupportActionBar(actionBar);

        titleTextView = (TextView) context.findViewById(R.id.title);
        titleTextView.setText(getString(R.string.root));
        backButton = (ImageButton) context.findViewById(R.id.back);
    }

    public void setToolbarBackAction(View.OnClickListener listener) {

        backButton.setOnClickListener(listener);
    }

    public void setToolbarTitle(@NonNull String title) {

        if (title.equals(File.separator)) {
            title = getString(R.string.root);
        }
        titleTextView.setText(title);
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

        return (view, position) -> {
            RemoteEntry entry = adapter.getData().get(position);
            if (entry.isFolder()) {
                presenter.listFolder(entry.asFolder().folderId());
            } else {
                RemoteFile remoteFile = entry.asFile();
                File localFolder = getContext().getFilesDir();
                presenter.downloadFile(remoteFile, new File(localFolder, remoteFile.name()));
            }
        };
    }

    private View.OnClickListener initToolBackBackButtonAction() {

        return view -> {
            if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
                presenter.listFolder(currentFolder.parentFolderId());
            }
        };
    }

    public void showLoading() {

        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getContext());
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setMessage(getString(R.string.loading_message));
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void hideLoading() {

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (currentFolder != null && currentFolder.folderId() != RemoteFolder.ROOT_FOLDER_ID) {
            presenter.listFolder(currentFolder.parentFolderId());
            return true;
        }
        return false;
    }

    @Override
    public void displayFolder(RemoteFolder folder) {

        int childCount = folder.children().size();
        noItemsView.setVisibility(childCount == 0 ? View.VISIBLE : GONE);
        recyclerView.setVisibility(childCount == 0 ? GONE : View.VISIBLE);
        if (childCount > 0) {
            List<RemoteEntry> entries = folder.children();
            adapter.setData(entries);
        }
        currentFolder = folder;
        setToolbarTitle(folder.name());
    }

    @Override
    public void displayError(BrowsingPresenter.Error error) {

        String errorMessage = "";
        switch (error) {
            case NO_INTERNET:
                errorMessage = getString(R.string.no_internet_error_message);
                break;

            case SOCKET_TIMEOUT:
                errorMessage = getString(R.string.connection_timeout_message);
                break;

            case IO:
                errorMessage = getString(R.string.file_not_found_message);
                break;
        }
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openFile(File file, String contentType) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID, file);
        intent.setDataAndType(uri, contentType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PackageManager pm = getContext().getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            getContext().startActivity(intent);
        } else {
            Toast.makeText(getContext(), R.string.file_open_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setLoadingState(boolean isLoading) {

        if (isLoading) {
            showLoading();
        } else {
            hideLoading();
        }
    }

    @Override
    public BrowsingPresenter createPresenter() {

        //TODO add manager
        return new BrowsingPresenter();
    }
}
