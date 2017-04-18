package com.example.xcomputers.testassignment.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xcomputers.testassignment.R;
import com.pcloud.sdk.RemoteEntry;


/**
 * Created by Georgi on 4/18/2017.
 */

public class BrowsingFolderViewHolder extends AbstractViewHolder {

    private TextView dirName;
    private ImageView dirImage;


    public BrowsingFolderViewHolder(View itemView) {
        super(itemView);
        dirName = (TextView) itemView.findViewById(R.id.directory_name_TV);
        dirImage = (ImageView) itemView.findViewById(R.id.file_image_IV);

        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(RemoteEntry entry) {

        dirName.setText(entry.name());
        dirImage.setImageResource(R.drawable.ic_folder);
    }
}
