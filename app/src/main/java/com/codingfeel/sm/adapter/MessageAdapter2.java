package com.codingfeel.sm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.model.MessageModel;
import com.codingfeel.sm.service.CommonService;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.views.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/27.
 */
public class MessageAdapter2<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int viewtype_head = 1;
    private final int viewtype_item = 2;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<MessageModel> messageModels;
    private MaterialDialog materialDialog = null;
    private int color;

    public MessageAdapter2(Context context, List<MessageModel> list) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.messageModels = list;
        color = context.getResources().getColor(R.color.themeColor_red);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessageModel messageModel = (MessageModel) messageModels.get(position);
        if (getItemViewType(position) == viewtype_head) {
            HeadViewHolder viewHolder = (HeadViewHolder) holder;
            viewHolder.tvMessageSystemName.setText(messageModel.getMessageTitle());
            viewHolder.tvMessageItemContent.setText(messageModel.getMessageContent());
            viewHolder.tvMessageItemTime.setText(DateUtil.date2Str(messageModel.getCreateTime(), DateUtil.FORMAT_YMDH_CN));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.intent2SystemMessageActivity(context);
                }
            });
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            ImageUtils.displayImage(context, messageModel.getAvatar(), viewHolder.ivMessageItemHead, R.mipmap.head_system);
            if (messageModel.getType() == ConstantValue.MESSAGE_TYPE_INFO_VERIFY || messageModel.getType() == ConstantValue.MESSAGE_TYPE_POST_VERIFY) {
                ((ViewHolder) holder).tvMessageItemName.setText(messageModel.getMessageTitle());
            } else {
                ((ViewHolder) holder).tvMessageItemName.setText(messageModel.getMessageExt1());
            }

            ((ViewHolder) holder).tvMessageItemContent.setText(messageModel.getMessageContent());

            ((ViewHolder) holder).tvMessageItemTime.setText(DateUtil.date2Str(messageModel.getCreateTime(), DateUtil.FORMAT_YMDH_CN));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
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


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    materialDialog = new MaterialDialog.Builder(context).title("提示").positiveColor(color).negativeColor(color).content("确定要删除么").positiveText("删除").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.POSITIVE) {
                                CommonService.getInstance().delMessage(messageModel.getUuid());
                                materialDialog.dismiss();
                            } else if (which == DialogAction.NEGATIVE) {
                                materialDialog.dismiss();
                            }

                        }
                    }).build();
                    materialDialog.show();
                    return true;
                }
            });
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case viewtype_head:
                return new HeadViewHolder(layoutInflater.inflate(R.layout.view_head_message, parent, false));
            case viewtype_item:
                return new ViewHolder(layoutInflater.inflate(R.layout.item_message, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return viewtype_head;
        }
        return viewtype_item;
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_message_system_head)
        CircleImageView ivMessageSystemHead;
        @BindView(R.id.tv_message_system_name)
        TextView tvMessageSystemName;
        @BindView(R.id.tv_message_item_time)
        TextView tvMessageItemTime;
        @BindView(R.id.tv_message_item_content)
        TextView tvMessageItemContent;
        @BindView(R.id.cv_message_system)
        CardView cvMessageSystem;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<MessageModel> getList() {
        return messageModels;
    }

    public void setList(List<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_message_item_head)
        CircleImageView ivMessageItemHead;
        @BindView(R.id.tv_message_item_name)
        TextView tvMessageItemName;
        @BindView(R.id.tv_message_item_time)
        TextView tvMessageItemTime;
        @BindView(R.id.tv_message_item_content)
        TextView tvMessageItemContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
