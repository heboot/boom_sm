package com.codingfeel.sm.adapter;

import android.content.Context;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Heboot on 16/7/10.
 */
public class InfoHomeAdapter<T> extends AutoRVAdapter {
    public InfoHomeAdapter(Context context, List<InfoModel> list) {
        super(context, list);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_info_home;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final InfoModel infoModel = (InfoModel) list.get(position);

        ImageUtils.displayImage(context, infoModel.getSmallImgUrl(), holder.getImageView(R.id.iv_home_item_img), R.mipmap.info_img_normal_red);


        ImageUtils.displayInfoMarkImage(context, infoModel.getType(), holder.getImageView(R.id.iv_home_item_mark));

        holder.getTextView(R.id.tv_home_item_title).setText(infoModel.getTitle());

        holder.getTextView(R.id.tv_home_item_info).setText(infoModel.getWebsiteName() + " | " + DateUtil.date2Str(infoModel.getCreateTime(), DateUtil.FORMAT_HM));

        holder.getTextView(R.id.tv_home_item_comment).setText(infoModel.getCommentCount() + "");

        holder.getTextView(R.id.tv_home_item_price).setText(infoModel.getSummary());

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2InfoDetailActivity(context, infoModel);
            }
        });

    }

    @Subscribe
    public void onEvent(final InfoEvent.InfoCommentUpdateEvent event) {

        for (int i = 0; i < list.size(); i++) {
            InfoModel infoModel = (InfoModel) list.get(i);
            if (infoModel.getInfoId().equals(event.getInfoId())) {
                infoModel = InfoService.getInstance().loadInfoModelsSync().get(event.getInfoId());
                list.add(i, infoModel);
                notifyItemChanged(i);
                return;
            }
        }


    }
}
