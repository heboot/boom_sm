package com.codingfeel.sm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.views.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/10.
 */
public class PostHomeAdapter2<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<PostModel> postModelList;
    private Context context;

    private final int viewtype_search = 1;
    private final int viewtype_item = 2;

    public PostHomeAdapter2(Context context, List<PostModel> d) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.postModelList = d;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case viewtype_search:
                return new SearchHolder(layoutInflater.inflate(R.layout.view_search, parent, false));
            case viewtype_item:
                return new ViewHolder(layoutInflater.inflate(R.layout.item_post_home, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PostModel postModel = postModelList.get(position);
        if (getItemViewType(position) == viewtype_search) {

        } else if (getItemViewType(position) == viewtype_item) {
            ViewHolder itemHolder = (ViewHolder) holder;

            ImageUtils.displayImage(context, postModel.getPostImgUrl(), itemHolder.ivPostHomeItemImg, R.mipmap.post_img_normal_gray);

            ImageUtils.displayImage(context, postModel.getAvatar(), itemHolder.ivPostHomeItemHead, R.mipmap.head_system);

            itemHolder.tvPostHomeItemTitle.setText(postModel.getTitle());

//        holder.getTextView(R.id.tv_home_item_price).setText(infoModel.getP);

            itemHolder.tvPostHomeItemTag.setText(PostService.getInstance().getPostType(context, postModel.getType()));


            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.intent2PostDetailActivity(context, postModel);
                }
            });
        }

    }

    public List<PostModel> getList() {
        return postModelList;
    }

    public void setList(List<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return viewtype_search;
        }
        return viewtype_item;
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    static class SearchHolder extends RecyclerView.ViewHolder {

        SearchHolder(View view) {
            super(view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_post_home_item_img)
        ImageView ivPostHomeItemImg;
        @BindView(R.id.tv_post_home_item_tag)
        TextView tvPostHomeItemTag;
        @BindView(R.id.rylt_post_home_item_img)
        RelativeLayout ryltPostHomeItemImg;
        @BindView(R.id.iv_post_home_item_head)
        CircleImageView ivPostHomeItemHead;
        @BindView(R.id.tv_post_home_item_title)
        TextView tvPostHomeItemTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
