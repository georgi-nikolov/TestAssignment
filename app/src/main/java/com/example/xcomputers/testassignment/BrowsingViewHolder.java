package com.example.xcomputers.testassignment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xComputers on 31/03/2017.
 */

public class BrowsingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView dirImage;
    TextView dirName;
    TextView dirCreationDate;
    TextView dirSize;


    public BrowsingViewHolder(View itemView) {
        super(itemView);
        dirImage = (ImageView) itemView.findViewById(R.id.directory_image_IV);
        dirName = (TextView) itemView.findViewById(R.id.directory_name_TV);
        dirCreationDate = (TextView) itemView.findViewById(R.id.creation_date_TV);
        dirSize = (TextView) itemView.findViewById(R.id.directory_size_TV);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // resultsItemClickListener.onResultClicked(v, getPosition());
    }

    public interface onResultClickListener {
        void onResultClicked(View view, int position);
    }

    public void setOnResultClickListener(final onResultClickListener mItemClickListener) {
        // this.resultsItemClickListener = mItemClickListener;
    }
}



