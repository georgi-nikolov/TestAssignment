package com.example.xcomputers.testassignment.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xcomputers.testassignment.R;
import com.example.xcomputers.testassignment.util.FileSizeExtractionUtil;
import com.pcloud.sdk.RemoteEntry;

/**
 * Created by Georgi on 4/18/2017.
 */

/**
 * The ViewHolder which takes care of the file type elements in the RecyclerView
 */
public class BrowsingFileViewHolder extends AbstractViewHolder {

    private ImageView dirImage;
    private TextView dirCreationDate;
    private TextView dirSize;
    private TextView fileName;

    public BrowsingFileViewHolder(View itemView) {

        super(itemView);
        dirImage = (ImageView) itemView.findViewById(R.id.file_image_IV);
        fileName = (TextView) itemView.findViewById(R.id.file_name_TV);
        dirCreationDate = (TextView) itemView.findViewById(R.id.creation_date_TV);
        dirSize = (TextView) itemView.findViewById(R.id.file_size_TV);

        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(RemoteEntry entry) {

        dirImage.setImageResource(R.drawable.ic_file);
        String size = FileSizeExtractionUtil.extract(entry.asFile().size());
        dirSize.setText(size);
        dirCreationDate.setText(formatDate(entry.lastModified()));
        fileName.setText(entry.name());
    }
}
