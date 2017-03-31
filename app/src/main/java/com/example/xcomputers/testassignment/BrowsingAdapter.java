package com.example.xcomputers.testassignment;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcloud.sdk.RemoteEntry;

import java.util.List;

/**
 * Created by xComputers on 31/03/2017.
 */

public class BrowsingAdapter extends RecyclerView.Adapter<BrowsingViewHolder> {

    private List<RemoteEntry> enries;
    private Activity context;

    public BrowsingAdapter(List<RemoteEntry> enries, Activity context) {
        this.enries = enries;
        this.context = context;
    }

    @Override
    public BrowsingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.file_view, parent, false);
        BrowsingViewHolder vh = new BrowsingViewHolder(row);
        return vh;
    }

    @Override
    public void onBindViewHolder(BrowsingViewHolder holder, int position) {

        RemoteEntry entry = enries.get(position);
        if (entry.isFile()) {
            holder.dirImage.setImageResource(R.drawable.ic_file);
            holder.dirSize.setText(String.valueOf(entry.asFile().size()));
            holder.dirCreationDate.setText(entry.lastModified().toString());
        } else if (entry.isFolder()) {
            holder.dirImage.setImageResource(R.drawable.ic_folder);
        }
        holder.dirName.setText(entry.name());
    }

    @Override
    public int getItemCount() {

        return enries.size();
    }
}
