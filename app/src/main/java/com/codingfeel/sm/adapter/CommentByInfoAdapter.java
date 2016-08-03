package com.codingfeel.sm.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.model.InfoCommentModel;
import com.codingfeel.sm.ui.common.CommentsActivity;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;

import java.util.List;

/**
 * Created by Heboot on 16/7/10.
 */
public class CommentByInfoAdapter<T> extends AutoRVAdapter {

    private CommentsActivity commentsActivity;

    public CommentByInfoAdapter(Context context, List<T> list, CommentsActivity activity) {
        super(context, list);
        this.commentsActivity = activity;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_comment;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final InfoCommentModel infoCommentModel = (InfoCommentModel) list.get(position);


        ImageUtils.displayImage(context, infoCommentModel.getAvatar(), holder.getImageView(R.id.iv_comment_item_head));


        if (TextUtils.isEmpty(infoCommentModel.getReNickName())) {
            holder.getTextView(R.id.tv_comment_item_renick).setVisibility(View.GONE);
            holder.getTextView(R.id.tv_comment_item_re).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.tv_comment_item_renick).setText(infoCommentModel.getNickName());
            holder.getTextView(R.id.tv_comment_item_renick).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.tv_comment_item_re).setVisibility(View.VISIBLE);
        }

        holder.getTextView(R.id.tv_comment_item_name).setText(infoCommentModel.getNickName());
        holder.getTextView(R.id.tv_comment_item_content).setText(infoCommentModel.getContent());

        holder.getTextView(R.id.tv_comment_item_time).setText(DateUtil.date2Str(infoCommentModel.getCreateTime(), DateUtil.FORMAT_HM));

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentsActivity.setReName(infoCommentModel.getNickName());
                commentsActivity.setReUid(infoCommentModel.getUid());
            }
        });


    }
}
