package com.codingfeel.sm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.model.PostTagModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/26.
 */
public class PostTagSpinnerAdapter extends BaseAdapter {

    private List<PostTagModel> postTagModelList;
    private Context context;

    public PostTagSpinnerAdapter(Context context, List<PostTagModel> postTagModels) {
        this.postTagModelList = postTagModels;
        this.context = context;
    }


    @Override
    public int getCount() {
        return postTagModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return postTagModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder viewHolder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_post_tag, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.tvPostTagName.setText(postTagModelList.get(i).getName());

        return view;


    }

    static class ViewHolder {
        @BindView(R.id.tv_post_tag_name)
        TextView tvPostTagName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
