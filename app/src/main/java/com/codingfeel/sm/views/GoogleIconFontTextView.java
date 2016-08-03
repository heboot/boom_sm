package com.codingfeel.sm.views;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class GoogleIconFontTextView extends TextView {
    private Context mContext;

    public GoogleIconFontTextView(Context context) {
        super(context);
        initView();
    }

    public GoogleIconFontTextView(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "regular.ttf");
        setTypeface(iconfont);
    }

}
