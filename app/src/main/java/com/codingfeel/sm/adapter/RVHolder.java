package com.codingfeel.sm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Heboot on 16/7/10.
 */
public class RVHolder  extends RecyclerView.ViewHolder {

    private ViewHolder viewHolder;

    public RVHolder(View itemView) {
        super(itemView);
        viewHolder=ViewHolder.getViewHolder(itemView);
    }


    public ViewHolder getViewHolder() {
        return viewHolder;
    }
}
