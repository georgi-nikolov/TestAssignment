package com.example.xcomputers.testassignment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.viewHolders.AbstractViewHolder;
import com.example.xcomputers.testassignment.viewHolders.BrowsingFileViewHolder;
import com.example.xcomputers.testassignment.viewHolders.BrowsingFolderViewHolder;
import com.pcloud.sdk.RemoteEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xComputers on 31/03/2017.
 */

public class BrowsingAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private static final int TYPE_FILE = 0;
    private static final int TYPE_FOLDER = 1;

    private List<RemoteEntry> entries;
    private OnResultClickListener resultsItemClickListener;

    public BrowsingAdapter() {

        this(Collections.emptyList());
    }

    public BrowsingAdapter(List<RemoteEntry> entries) {

        this.entries = entries;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        AbstractViewHolder holder = null;
        LayoutInflater inflater;
        View row;
        switch (viewType) {
            case TYPE_FILE:
                inflater = LayoutInflater.from(parent.getContext());
                row = inflater.inflate(R.layout.file_view, parent, false);
                holder = new BrowsingFileViewHolder(row);
                break;
            case TYPE_FOLDER:
                inflater = LayoutInflater.from(parent.getContext());
                row = inflater.inflate(R.layout.folder_view, parent, false);
                holder = new BrowsingFolderViewHolder(row);
                break;
        }
        if (holder != null) {
            holder.setOnClickListener(resultsItemClickListener);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {

        RemoteEntry entry = entries.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemViewType(int position) {

        int type = -1;
        if (entries.get(position).isFile()) {
            type = TYPE_FILE;
        } else if (entries.get(position).isFolder()) {
            type = TYPE_FOLDER;
        }
        return type;
    }

    @Override
    public int getItemCount() {

        return entries.size();
    }

    /**
     * Changes the data this adapter works with
     *
     * @param entries List<RemoteEntry> to set as the new data
     */
    public void setData(List<RemoteEntry> entries) {

        if (entries != null) {
            this.entries = new ArrayList<>(entries);
            notifyDataSetChanged();
        }
    }

    /**
     * Basic getter for the data currently in the adapter
     *
     * @return A List with the items the adapter is currently working with
     */
    public List<RemoteEntry> getData() {
        return this.entries;
    }

    /**
     * An interface to provide on click functionality for each element of the RecyclerView
     */
    public interface OnResultClickListener {
        /**
         * A callback method to be fired upon a click on an item of the RecyclerView
         *
         * @param view     The view that was clicked
         * @param position The position of the view in the adapter
         */
        void onResultClicked(View view, int position);
    }

    /**
     * Call to provide implementation of the onClick method for the RecyclerView
     *
     * @param mItemClickListener A an implementation for the onClick logic
     */
    public void setOnResultClickListener(final OnResultClickListener mItemClickListener) {
        this.resultsItemClickListener = mItemClickListener;
    }
}
