package com.codingfeel.sm.views.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.utils.WXUtil;

/**
 * Created by Heboot on 16/7/29.
 */
public class PopupShare extends PopupWindow {


    private TextView tvShareWx;
    private LinearLayout llytContainer;


    public PopupShare(final Context context, final String url, final String title, final String desc) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        this.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x55000000);
        this.setBackgroundDrawable(dw);

        tvShareWx = (TextView) view.findViewById(R.id.tv_share_wx);
        llytContainer = (LinearLayout) view.findViewById(R.id.llyt_share_container);
        tvShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXUtil.getInstance(context).shareWebpageObject(url, title, desc);
            }
        });
        llytContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.update();
        setContentView(view);


    }


}
