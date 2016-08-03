package com.codingfeel.sm.ui.my;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codingfeel.sm.R;
import com.codingfeel.sm.event.SystemEvent;
import com.codingfeel.sm.service.CommonService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.DownloadUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.VerisonUtil;
import com.codingfeel.sm.views.MaterialRippleLayout;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/28.
 */
public class SettingActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.llyt_setting_version_update)
    LinearLayout llytSettingVersionUpdate;
    @BindView(R.id.rply_setting_version_update)
    MaterialRippleLayout rplySettingVersionUpdate;
    @BindView(R.id.llyt_setting_clean_cache)
    LinearLayout llytSettingCleanCache;
    @BindView(R.id.rply_setting_clean_cache)
    MaterialRippleLayout rplySettingCleanCache;
    @BindView(R.id.llyt_setting_contact_weixinamount)
    LinearLayout llytSettingContactWeixinamount;
    @BindView(R.id.rply_setting_contact_weixinamount)
    MaterialRippleLayout rplySettingContactWeixinamount;
    @BindView(R.id.llyt_setting_contact_weibo)
    LinearLayout llytSettingContactWeibo;
    @BindView(R.id.rply_setting_contact_weibo)
    MaterialRippleLayout rplySettingContactWeibo;
    @BindView(R.id.llyt_setting_contact_email)
    LinearLayout llytSettingContactEmail;
    @BindView(R.id.rply_setting_contact_email)
    MaterialRippleLayout rplySettingContactEmail;
    @BindView(R.id.tv_setting_share_app)
    TextView tvSettingShareApp;
    @BindView(R.id.rply_setting_share_app)
    MaterialRippleLayout rplySettingShareApp;
    @BindView(R.id.tv_setting_share_weixinamount)
    TextView tvSettingShareWeixinamount;
    @BindView(R.id.rply_setting_share_weixinamount)
    MaterialRippleLayout rplySettingShareWeixinamount;
    @BindView(R.id.tv_setting_feedback)
    TextView tvSettingFeedback;
    @BindView(R.id.rply_setting_feedback)
    MaterialRippleLayout rplySettingFeedback;
    @BindView(R.id.tv_setting_about)
    TextView tvSettingAbout;
    @BindView(R.id.rply_setting_about)
    MaterialRippleLayout rplySettingAbout;
    @BindView(R.id.tv_setting_version)
    TextView tvSettingVersion;


    private int color;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_setting), true, null);
        ButterKnife.bind(this);
        initListener();
        initVerisonData();
    }


    private void initVerisonData() {
        String verisonName = VerisonUtil.getVersionName(this);
        tvSettingVersion.setText(verisonName);

    }


    private void initListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        llytSettingVersionUpdate.setOnClickListener(this);
        llytSettingCleanCache.setOnClickListener(this);
        llytSettingContactWeixinamount.setOnClickListener(this);
        llytSettingContactWeibo.setOnClickListener(this);
        llytSettingContactEmail.setOnClickListener(this);
        tvSettingShareWeixinamount.setOnClickListener(this);
        tvSettingShareApp.setOnClickListener(this);
        tvSettingFeedback.setOnClickListener(this);
        tvSettingAbout.setOnClickListener(this);
    }

    @Subscribe
    public void onEvent(final SystemEvent.VerisonUpdateEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(event.getCommonGuestBean().getAppVersion())) {
                    int versionCode = VerisonUtil.getVersion(SettingActivity.this);
                    int serverVersionCode = Integer.parseInt(event.getCommonGuestBean().getAppVersion().replace(".", ""));

                    if (versionCode < serverVersionCode) {


                        if (Integer.parseInt(event.getCommonGuestBean().getAppIsForceUpdate()) == 1) {
                            materialDialog = new MaterialDialog.Builder(SettingActivity.this).title("新版本").positiveColor(color).negativeColor(color).content(event.getCommonGuestBean().getAppUpdateNotes()).cancelable(false).positiveText("更新").onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    if (which == DialogAction.POSITIVE) {
                                        doUpdateVerison("", event.getCommonGuestBean().getAppDownloadUrl());
                                        materialDialog.dismiss();
                                    }

                                }
                            }).build();
                        } else {
                            materialDialog = new MaterialDialog.Builder(SettingActivity.this).title("新版本").cancelable(true).positiveColor(color).negativeColor(color).content(event.getCommonGuestBean().getAppUpdateNotes()).positiveText("更新").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    if (which == DialogAction.POSITIVE) {
                                        doUpdateVerison("", event.getCommonGuestBean().getAppDownloadUrl());
                                        materialDialog.dismiss();
                                    } else if (which == DialogAction.NEGATIVE) {
                                        materialDialog.dismiss();
                                    }

                                }
                            }).build();
                        }

                        materialDialog.show();

                    } else {
                        showToast("已经是最新版本了", SuperToast.Duration.VERY_SHORT);
                    }
                }
            }
        });
    }

    private void doUpdateVerison(String content, final String url) {
        new MaterialDialog.Builder(SettingActivity.this)
                .title("新版本")
                .content("正在下载")
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 0, true)
                .cancelable(false)
                .widgetColor(R.color.themeColor_red)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
                        DownloadUtils.downloadFile(SettingActivity.this, url, 0, dialog);
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_setting_version_update:
                CommonService.getInstance().homeGuest(SettingActivity.this);
                break;
            case R.id.llyt_setting_clean_cache:
                break;
            case R.id.llyt_setting_contact_weixinamount:
                break;
            case R.id.llyt_setting_contact_weibo:
                break;
            case R.id.llyt_setting_contact_email:
                break;
            case R.id.tv_setting_share_app:
                break;
            case R.id.tv_setting_share_weixinamount:
                break;
            case R.id.tv_setting_feedback:
                IntentUtils.intent2FeedbackActivity(SettingActivity.this);
                break;
            case R.id.tv_setting_about:
                break;
        }
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
