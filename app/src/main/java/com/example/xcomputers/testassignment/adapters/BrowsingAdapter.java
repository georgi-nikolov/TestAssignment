package com.example.xcomputers.testassignment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.FileSizeExtractionUtil;
import com.pcloud.sdk.RemoteEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by xComputers on 31/03/2017.
 */

public class BrowsingAdapter extends RecyclerView.Adapter<BrowsingAdapter.BrowsingViewHolder> {

    private List<RemoteEntry> entries;
    private OnResultClickListener resultsItemClickListener;
    

    public BrowsingAdapter(List<RemoteEntry> entries) {
        this.entries = entries;
    }

    @Override
    public BrowsingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.file_view, parent, false);
        return new BrowsingViewHolder(row);
    }

    @Override
    public void onBindViewHolder(BrowsingViewHolder holder, int position) {

        RemoteEntry entry = entries.get(position);
        if (entry.isFile()) {
            holder.dirCreationDate.setVisibility(VISIBLE);
            holder.dirSize.setVisibility(VISIBLE);
            holder.fileName.setVisibility(VISIBLE);
            holder.dirName.setVisibility(GONE);

            holder.dirImage.setImageResource(R.drawable.ic_file);
            String size = FileSizeExtractionUtil.extract(entry.asFile().size());
            holder.dirSize.setText(size);
            holder.dirCreationDate.setText(formatDate(entry.lastModified()));
            holder.fileName.setText(entry.name());

        } else if (entry.isFolder()) {
            holder.dirCreationDate.setVisibility(GONE);
            holder.dirSize.setVisibility(GONE);
            holder.fileName.setVisibility(GONE);
            holder.dirName.setVisibility(VISIBLE);
            holder.dirName.setText(entry.name());
            holder.dirImage.setImageResource(R.drawable.ic_folder);
        }
    }

    private String formatDate(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return formatter.format(date);
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

        this.entries = entries;
        notifyDataSetChanged();
    }

    /**
     * Basic getter for the data currently in the adapter
     *
     * @return A List with the items the adapter is currently working with
     */
    public List<RemoteEntry> getData() {
        return this.entries;
    }

    class BrowsingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView dirImage;
        private TextView dirName;
        private TextView dirCreationDate;
        private TextView dirSize;
        private TextView fileName;

        BrowsingViewHolder(View itemView) {
            super(itemView);
            dirImage = (ImageView) itemView.findViewById(R.id.directory_image_IV);
            dirName = (TextView) itemView.findViewById(R.id.directory_name_TV);
            fileName = (TextView) itemView.findViewById(R.id.file_name_TV);
            dirCreationDate = (TextView) itemView.findViewById(R.id.creation_date_TV);
            dirSize = (TextView) itemView.findViewById(R.id.directory_size_TV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            resultsItemClickListener.onResultClicked(v, getPosition());
        }

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
