package com.codingfeel.sm.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.model.PostCommentModel;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;

import java.util.List;

/**
 * Created by Heboot on 16/7/10.
 */
public class CommentByPostAdapter<T> extends AutoRVAdapter {
    public CommentByPostAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_comment;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostCommentModel postCommentModel = (PostCommentModel) list.get(position);


        ImageUtils.displayImage(context, postCommentModel.getAvatar(), holder.getImageView(R.id.iv_comment_item_head));

        holder.getTextView(R.id.tv_comment_item_name).setText(postCommentModel.getNickName());

        holder.getTextView(R.id.tv_comment_item_content).setText(postCommentModel.getContent());

        if (TextUtils.isEmpty(postCommentModel.getReNickName())) {
            holder.getTextView(R.id.tv_comment_item_renick).setVisibility(View.GONE);
            holder.getTextView(R.id.tv_comment_item_re).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.tv_comment_item_renick).setText(postCommentModel.getNickName());
            holder.getTextView(R.id.tv_comment_item_renick).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.tv_comment_item_re).setVisibility(View.VISIBLE);
        }
        holder.getTextView(R.id.tv_comment_item_content).setText(postCommentModel.getContent());

        holder.getTextView(R.id.tv_comment_item_time).setText(DateUtil.date2Str(postCommentModel.getCreateTime(), DateUtil.FORMAT_HM));


    }
}
