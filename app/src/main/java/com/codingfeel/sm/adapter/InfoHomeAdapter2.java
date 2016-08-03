package com.codingfeel.sm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/10.
 */
public class InfoHomeAdapter2<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<InfoModel> infoModelList;
    private Context context;

    private final int viewtype_search = 1;
    private final int viewtype_item = 2;

    public InfoHomeAdapter2(Context context, List<InfoModel> d) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.infoModelList = d;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case viewtype_search:
                return new SearchHolder(layoutInflater.inflate(R.layout.view_search, parent, false));
            case viewtype_item:
                return new ViewHolder(layoutInflater.inflate(R.layout.item_info_home, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final InfoModel infoModel = infoModelList.get(position);
        if (getItemViewType(position) == viewtype_search) {

        } else if (getItemViewType(position) == viewtype_item) {
            ViewHolder itemHolder = (ViewHolder) holder;

            ImageUtils.displayImage(context, infoModel.getSmallImgUrl(), itemHolder.ivHomeItemImg, R.mipmap.info_img_normal_red);

            ImageUtils.displayInfoMarkImage(context, infoModel.getType(), itemHolder.ivHomeItemMark);

            itemHolder.tvHomeItemTitle.setText(infoModel.getTitle());

//        holder.getTextView(R.id.tv_home_item_price).setText(infoModel.getP);

            itemHolder.tvHomeItemInfo.setText(infoModel.getWebsiteName() + " | " + DateUtil.date2Str(infoModel.getCreateTime(), DateUtil.FORMAT_HM));

            itemHolder.tvHomeItemPrice.setText(infoModel.getSummary());

            itemHolder.tvHomeItemGood.setText(infoModel.getCommentCount() + "");

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.intent2InfoDetailActivity(context, infoModel);
                }
            });
        }

    }

    public List<InfoModel> getList() {
        return infoModelList;
    }

    public void setList(List<InfoModel> infoModelList) {
        this.infoModelList = infoModelList;
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
        return infoModelList.size();
    }

    static class SearchHolder extends RecyclerView.ViewHolder {

        SearchHolder(View view) {
            super(view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_home_item_img)
        ImageView ivHomeItemImg;
        @BindView(R.id.tv_home_item_title)
        TextView tvHomeItemTitle;
        @BindView(R.id.tv_home_item_price)
        TextView tvHomeItemPrice;
        @BindView(R.id.tv_home_item_info)
        TextView tvHomeItemInfo;
        @BindView(R.id.tv_home_item_comment)
        TextView tvHomeItemGood;
        @BindView(R.id.iv_home_item_mark)
        ImageView ivHomeItemMark;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
