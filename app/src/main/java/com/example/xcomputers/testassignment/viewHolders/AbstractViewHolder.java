package com.example.xcomputers.testassignment.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.xcomputers.testassignment.adapters.BrowsingAdapter;
import com.pcloud.sdk.RemoteEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Georgi on 4/18/2017.
 */

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private BrowsingAdapter.OnResultClickListener resultsItemClickListener;

    AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(RemoteEntry entry);

    @Override
    public void onClick(View v) {
        if(getAdapterPosition() == RecyclerView.NO_POSITION){
            return;
        }
        resultsItemClickListener.onResultClicked(v, getAdapterPosition());
    }

    String formatDate(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return formatter.format(date);
    }

    public void setOnClickListener(BrowsingAdapter.OnResultClickListener listener){

        this.resultsItemClickListener = listener;
    }

}
