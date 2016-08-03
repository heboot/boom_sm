package com.codingfeel.sm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Heboot on 16/7/10.
 */
public class MyPostAdapter<T> extends AutoRVAdapter {
    public MyPostAdapter(Context context, List<T> list) {
        super(context, list);
        color = context.getResources().getColor(R.color.themeColor_red);
    }
    private MaterialDialog materialDialog = null;
    private int color;
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_post_mypost;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostModel postModel = (PostModel) list.get(position);

        ImageUtils.displayImage(context, postModel.getPostImgUrl(), holder.getImageView(R.id.iv_mypost_item_img),R.mipmap.post_img_normal_gray);

//        ImageUtils.displayImage(context, postModel.getAvatar(), holder.getImageView(R.id.iv_post_home_item_head));

        Picasso.with(context).load(R.mipmap.head_system).into(holder.getImageView(R.id.iv_mypost_item_head));


        switch (postModel.getStatus()) {
            case ConstantValue.VERIFY_STATUS_ING:
                holder.getTextView(R.id.tv_mypost_verify).setText("审核中");
                break;
            case ConstantValue.VERIFY_STATUS_ERR:
                holder.getTextView(R.id.tv_mypost_verify).setText("审核失败");
                break;
            case ConstantValue.VERIFY_STATUS_SUC:
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.intent2PostDetailActivity(context, postModel);
                    }
                });
                break;
        }



        materialDialog = new MaterialDialog.Builder(context).title("提示").positiveColor(color).negativeColor(color).content("确定要删除么").positiveText("删除").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                if (which == DialogAction.POSITIVE) {
                    UserService.getInstance().delPost(postModel.getPostId());
                    materialDialog.dismiss();
                } else if (which == DialogAction.NEGATIVE) {
                    materialDialog.dismiss();
                }

            }
        }).build();
        holder.getTextView(R.id.tv_mypost_item_title).setText(postModel.getTitle());
        holder.getTextView(R.id.tv_mypost_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.show();
            }
        });
        holder.getTextView(R.id.tv_mypost_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2EditPostActivity(context, postModel);
            }
        });


    }
}
