package com.codingfeel.sm.adapter;

import android.content.Context;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Heboot on 16/7/10.
 */
public class PostHomeAdapter<T> extends AutoRVAdapter {
    public PostHomeAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_post_home;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostModel postModel = (PostModel) list.get(position);

        ImageUtils.displayImage(context, postModel.getPostImgUrl(), holder.getImageView(R.id.iv_post_home_item_img), R.mipmap.post_img_normal_gray);


//        ImageUtils.displayImage(context, postModel.getAvatar(), holder.getImageView(R.id.iv_post_home_item_head));

        Picasso.with(context).load(R.mipmap.head_system).into(holder.getImageView(R.id.iv_post_home_item_head));

        holder.getTextView(R.id.tv_post_home_item_title).setText(postModel.getTitle());

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2PostDetailActivity(context, postModel);
            }
        });


        holder.getTextView(R.id.tv_post_home_item_tag).setText(PostService.getInstance().getPostType(context, postModel.getType()));

    }
}
