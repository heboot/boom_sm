package com.codingfeel.sm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codingfeel.sm.R;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Heboot on 16/7/28.
 */
public class MyFavAdapter<T> extends AutoRVAdapter {

    public MyFavAdapter(Context context, List<T> list) {
        super(context, list);
        color = context.getResources().getColor(R.color.themeColor_red);
    }

    private MaterialDialog materialDialog = null;
    private int color;

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_post_myfav;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostModel postModel = (PostModel) list.get(position);

        ImageUtils.displayImage(context, postModel.getPostImgUrl(), holder.getImageView(R.id.iv_myfav_item_img),R.mipmap.post_img_normal_gray);

        materialDialog = new MaterialDialog.Builder(context).title("提示").positiveColor(color).negativeColor(color).content("确定要删除么").positiveText("删除").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                if (which == DialogAction.POSITIVE) {
                    PostService.getInstance().fav(postModel.getPostId());
                    materialDialog.dismiss();
                } else if (which == DialogAction.NEGATIVE) {
                    materialDialog.dismiss();
                }

            }
        }).build();
//
//        materialDialog.setPositiveButton("删除", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PostService.getInstance().fav(postModel.getPostId());
//                materialDialog.dismiss();
//            }
//        }).setNegativeButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                materialDialog.dismiss();
//            }
//        });
        Picasso.with(context).load(R.mipmap.head_system).into(holder.getImageView(R.id.iv_myfav_item_head));

        holder.getTextView(R.id.tv_myfav_item_title).setText(postModel.getTitle());
        holder.getImageView(R.id.tv_myfav_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                materialDialog.show();
            }
        });

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2PostDetailActivity(context, postModel);
            }
        });


    }
}
