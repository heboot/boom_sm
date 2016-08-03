package com.codingfeel.sm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;

import java.util.List;


/**
 * Created by Heboot on 16/7/10.
 */
public class MyInfoAdapter<T> extends AutoRVAdapter {
    public MyInfoAdapter(Context context, List<T> list) {
        super(context, list);
        color = context.getResources().getColor(R.color.themeColor_red);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_info_myinfo;
    }

    private MaterialDialog materialDialog = null;
    private int color;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final InfoModel infoModel = (InfoModel) list.get(position);

        ImageUtils.displayImage(context, infoModel.getSmallImgUrl(), holder.getImageView(R.id.iv_myinfo_item_img),R.mipmap.head_system);

        holder.getTextView(R.id.tv_myinfo_item_title).setText(infoModel.getTitle());

        switch (infoModel.getStatus()) {
            case ConstantValue.VERIFY_STATUS_ING:
                holder.getTextView(R.id.tv_myinfo__verify).setText("审核中");
//                holder.getRlyt(R.id.rlyt_myinfo_verify).setVisibility(View.VISIBLE);
                break;
            case ConstantValue.VERIFY_STATUS_SUC:
                holder.getTextView(R.id.tv_myinfo__verify).setText("审核通过");
//                holder.getRlyt(R.id.rlyt_myinfo_verify).setVisibility(View.GONE);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.intent2InfoDetailActivity(context, infoModel);
                    }
                });
                break;
            case ConstantValue.VERIFY_STATUS_ERR:
                holder.getTextView(R.id.tv_myinfo__verify).setText("审核失败");
                break;
        }

        holder.getTextView(R.id.tv_myinfo_item_info).setText(infoModel.getWebsiteName() + " | " + DateUtil.date2Str(infoModel.getCreateTime(), DateUtil.FORMAT_HM));

        holder.getTextView(R.id.tv_myinfo_item_good).setText(infoModel.getCommentCount() + "");


        materialDialog = new MaterialDialog.Builder(context).title("提示").positiveColor(color).negativeColor(color).content("确定要删除么").positiveText("删除").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                if (which == DialogAction.POSITIVE) {
                    UserService.getInstance().delInfo(infoModel.getInfoId());
                    materialDialog.dismiss();
                } else if (which == DialogAction.NEGATIVE) {
                    materialDialog.dismiss();
                }

            }
        }).build();


        holder.getTextView(R.id.tv_myinfo_del).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           materialDialog.show();
                                       }
                                   }

                );


    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }


}
