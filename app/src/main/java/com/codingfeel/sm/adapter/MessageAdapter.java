package com.codingfeel.sm.adapter;

import android.content.Context;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.model.MessageModel;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;

import java.util.List;

/**
 * Created by Heboot on 16/7/27.
 */
public class MessageAdapter<T> extends AutoRVAdapter {
    public MessageAdapter(Context context, List<MessageModel> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_message;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MessageModel messageModel = (MessageModel) list.get(position);

        ImageUtils.displayImage(context, messageModel.getAvatar(), holder.getImageView(R.id.iv_message_item_head), R.mipmap.head_system);

        if (messageModel.getType() == ConstantValue.MESSAGE_TYPE_INFO_VERIFY || messageModel.getType() == ConstantValue.MESSAGE_TYPE_POST_VERIFY) {
            holder.getTextView(R.id.tv_message_item_name).setText(messageModel.getMessageTitle());
        } else {
            holder.getTextView(R.id.tv_message_item_name).setText(messageModel.getMessageExt1());
        }

        holder.getTextView(R.id.tv_message_item_content).setText(messageModel.getMessageContent());

        holder.getTextView(R.id.tv_message_item_time).setText(DateUtil.date2Str(messageModel.getCreateTime(), DateUtil.FORMAT_YMDH_CN));

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (messageModel.getType()) {
                    case ConstantValue.MESSAGE_TYPE_COMMENT_INFO:
                    case ConstantValue.MESSAGE_TYPE_COMMENT_INFO_RE:
                        IntentUtils.intent2InfoCommentActivity(context, messageModel.getInfoId(), null);
                        break;
                    case ConstantValue.MESSAGE_TYPE_COMMENT_POST:
                    case ConstantValue.MESSAGE_TYPE_COMMENT_POST_RE:
                        IntentUtils.intent2PostCommentActivity(context, messageModel.getPostId(), null);
                        break;
                    case ConstantValue.MESSAGE_TYPE_POST_VERIFY:
                        IntentUtils.intent2MyPostActivity(context);
                        break;
                    case ConstantValue.MESSAGE_TYPE_INFO_VERIFY:
                        IntentUtils.intent2MyInfoActivity(context);
                        break;

                }
            }
        });
    }
}
