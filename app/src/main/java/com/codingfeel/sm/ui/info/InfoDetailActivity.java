package com.codingfeel.sm.ui.info;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.enums.HtmlFrom;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.DrawableCenterTextView;
import com.codingfeel.sm.views.popup.PopupShare;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/13.
 */
public class InfoDetailActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_info_detail_img)
    ImageView ivInfoDetailImg;
    @BindView(R.id.tv_info_detail_websiteName)
    TextView tvInfoDetailWebsiteName;
    @BindView(R.id.tv_info_detail_time)
    TextView tvInfoDetailTime;
    @BindView(R.id.tv_info_detail_name)
    TextView tvInfoDetailName;
    @BindView(R.id.tv_info_detail_title)
    TextView tvInfoDetailTitle;
    @BindView(R.id.tv_info_detail_summary)
    TextView tvInfoDetailSummary;
    @BindView(R.id.tv_info_detail_content)
    WebView tvInfoDetailContent;
    @BindView(R.id.tv_info_detail_good)
    DrawableCenterTextView tvInfoDetailGood;
    @BindView(R.id.tv_info_detail_comment)
    DrawableCenterTextView tvInfoDetailComment;
    @BindView(R.id.tv_info_detail_share)
    DrawableCenterTextView tvInfoDetailShare;
    @BindView(R.id.tv_info_detail_link)
    TextView tvInfoDetailLink;
    @BindView(R.id.llyt_infodetail_bottom)
    LinearLayout llytInfodetailBottom;
    @BindView(R.id.llyt_infodetail_bottom_good)
    LinearLayout llytInfodetailBottomGood;
    @BindView(R.id.llyt_infodetail_bottom_comment)
    LinearLayout llytInfodetailBottomComment;
    @BindView(R.id.llyt_infodetail_bottom_share)
    LinearLayout llytInfodetailBottomShare;
    @BindView(R.id.iv_info_detail_good)
    ImageView ivInfoDetailGood;
    @BindView(R.id.rlyt_info_detail_container)
    RelativeLayout rlytInfoDetailContainer;

    private InfoModel infoModel;

    private String infoId;

    private PopupShare popupShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_detail_info), true, null);
        infoModel = (InfoModel) getIntent().getExtras().get(ContentKey.INFO_MODEL);
        if (infoModel != null) {
            showEvaView(infoModel.isEva());
            initData(infoModel);
        } else {
            infoId = (String) getIntent().getExtras().get(ContentKey.INFO_ID);
            InfoService.getInstance().detail(infoId);
        }

        initWebView();
        initListener();

    }

    private void initListener() {
        llytInfodetailBottomGood.setOnClickListener(this);
        llytInfodetailBottomComment.setOnClickListener(this);
        llytInfodetailBottomShare.setOnClickListener(this);
        tvInfoDetailLink.setOnClickListener(this);
    }


    private void initData(InfoModel infoModel) {


        if (infoModel != null) {

            ImageUtils.displayImage(this, infoModel.getBigImgUrl(), ivInfoDetailImg, R.mipmap.post_img_normal_gray);

            tvInfoDetailWebsiteName.setText(infoModel.getWebsiteName());

            tvInfoDetailTime.setText(DateUtil.date2Str(infoModel.getUpdateTime(), DateUtil.FORMAT_HM));

//            tvInfoDetailName.setText(infoModel.get);

            tvInfoDetailTitle.setText(infoModel.getTitle());

            tvInfoDetailSummary.setText(infoModel.getSummary());

//            tvInfoDetailContent.setText(Html.fromHtml(infoModel.getContent()));


//            tvInfoDetailContent.loadData(infoModel.getContent(), "text/html; charset=UTF-8", null);


            String url = BuildConfig.H5SERVER + "/info.html?infoId=" + infoModel.getInfoId() + "&app=1";

            tvInfoDetailContent.loadUrl(url);


            tvInfoDetailGood.setText(infoModel.getEvaCount() + "");

            tvInfoDetailComment.setText(infoModel.getCommentCount() + "");

            tvInfoDetailShare.setText(infoModel.getShareCount() + "");


        }


    }

    @Subscribe
    public void onEvent(final InfoEvent.InfoDetailEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ValidateUtil.hasError(event.getInfoModel())) {
                    showToast(event.getInfoModel().getError_text(), SuperToast.Duration.VERY_SHORT);
                }else{
                    initData(event.getInfoModel());
                    initEvaView(event.getInfoModel().isEva());
                }
            }
        });
    }


    @Subscribe
    public void onEvent(final InfoEvent.InfoShareSucEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInfoDetailShare.setText(infoModel.getShareCount() + 1);
                infoModel.setShareCount(infoModel.getShareCount() + 1);
                InfoService.getInstance().share(infoModel.getInfoId());
            }
        });
    }

    @Subscribe
    public void onEvent(final InfoEvent.InfoCommentUpdateEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoModel = InfoService.getInstance().loadInfoModelsSync().get(infoModel.getInfoId());
                tvInfoDetailComment.setText(infoModel.getCommentCount() + "");
            }
        });
    }

    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getBasicEventEnum() == BasicEventEnum.INFO_EVA) {
                    if (!ValidateUtil.hasError(event.getBaseModel())) {
                        initEvaView(infoModel.isEva());
                        EventBus.getDefault().post(new InfoEvent.InfoEvaUpdateEvent(infoModel.getInfoId()));
                    }
                }
            }
        });
    }

    private void initEvaView(boolean isEva) {
        if (isEva) {
            InfoService.getInstance().updateInfoModelEvaCount(infoModel.getInfoId(), infoModel.getEvaCount() - 1, false);
            infoModel = InfoService.getInstance().loadInfoModelsSync().get(infoModel.getInfoId());
//                            infoModel.setEva(false);
            tvInfoDetailGood.setText(infoModel.getEvaCount() + "");
            ivInfoDetailGood.setBackgroundResource(R.mipmap.good_normal);
        } else {
            InfoService.getInstance().updateInfoModelEvaCount(infoModel.getInfoId(), infoModel.getEvaCount() + 1, true);
            infoModel = InfoService.getInstance().loadInfoModelsSync().get(infoModel.getInfoId());
//                            infoModel.setEva(true);
            tvInfoDetailGood.setText(infoModel.getEvaCount() + "");
            ivInfoDetailGood.setBackgroundResource(R.mipmap.good_focus);
        }
    }

    private void showEvaView(boolean isEva) {
        if (!isEva) {
            tvInfoDetailGood.setText(infoModel.getEvaCount() + "");
            ivInfoDetailGood.setBackgroundResource(R.mipmap.good_normal);
        } else {
            tvInfoDetailGood.setText(infoModel.getEvaCount() + "");
            ivInfoDetailGood.setBackgroundResource(R.mipmap.good_focus);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_infodetail_bottom_good:
                InfoService.getInstance().eva(infoModel.getInfoId());
                break;
            case R.id.llyt_infodetail_bottom_comment:
                IntentUtils.intent2InfoCommentActivity(InfoDetailActivity.this, infoModel.getInfoId(), infoModel);
                break;
            case R.id.llyt_infodetail_bottom_share:
                String url = BuildConfig.H5SERVER + "/info.html?infoId=" + infoModel.getInfoId() + "&app=0";
                popupShare = new PopupShare(InfoDetailActivity.this, url, infoModel.getTitle(), infoModel.getSummary());
//                popupShare.showAtLocation(rlytInfoDetailContainer, Gravity.BOTTOM, 0, 0);
                popupShare.setFocusable(true);
                popupShare.setOutsideTouchable(true);
                popupShare.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                LogUtils.e(TAG, "================isshow" + popupShare.isShowing());
                break;
            case R.id.tv_info_detail_link:
                IntentUtils.intent2HmtlActivity(InfoDetailActivity.this, infoModel.getGoodsUrl(), HtmlFrom.INFO);
                break;

        }
    }


    private void initWebView() {
        tvInfoDetailContent.getSettings().setJavaScriptEnabled(true);
        tvInfoDetailContent.getSettings().setDefaultTextEncodingName("UTF-8");
        tvInfoDetailContent.getSettings().setSupportZoom(false);
        tvInfoDetailContent.getSettings().setDisplayZoomControls(false);
        // 缩放按钮
        tvInfoDetailContent.getSettings().setBuiltInZoomControls(true);
        tvInfoDetailContent.getSettings().setUseWideViewPort(true);

    }

    private void loadWebView() {
        tvInfoDetailContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    tvInfoDetailContent.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
