package com.codingfeel.sm.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.ImageEvent;
import com.codingfeel.sm.event.QiNiuEvent;
import com.codingfeel.sm.model.UserModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.MultiImageUtil;
import com.codingfeel.sm.utils.QiNiuUtil;
import com.codingfeel.sm.views.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Heboot on 16/7/29.
 */
public class UserInfoActivity extends ToolBarActivity implements View.OnClickListener {


    @BindView(R.id.iv_my_info_head)
    CircleImageView ivMyInfoHead;
    @BindView(R.id.tv_my_info_nickname)
    TextView tvMyInfoNickname;
    @BindView(R.id.rlyt_myinfo_head)
    RelativeLayout rlytMyinfoHead;
    @BindView(R.id.rlyt_myinfo_nickname)
    RelativeLayout rlytMyinfoNickname;

    private MultiImageSelector multiImageSelector;

    private ArrayList<String> mSelectPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_userinfo);
        showToolBar(getResources().getString(R.string.page_title_feedback), true, null);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
        initListener();
    }

    private void initData() {

        UserModel userModel = UserService.getInstance().getSharePrefUser(this);

        ImageUtils.displayImage(this, userModel.getAvatar(), ivMyInfoHead);

        tvMyInfoNickname.setText(userModel.getNickName());

    }


    private void initListener() {
        multiImageSelector = MultiImageUtil.getInstance().getMultiImageSelector(mSelectPath);
        rlytMyinfoHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiImageSelector.start(UserInfoActivity.this, ConstantValue.REQUEST_IMAGE);
            }
        });
        rlytMyinfoNickname.setOnClickListener(this);
    }


    @Subscribe
    public void onEvent(final ImageEvent.ImageCropEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(event.getImgPath())) {
                    //上传到七牛
                    QiNiuUtil.doUpload(event.getImgPath(), ConstantValue.QINIU_BUCKETNAME_HEAD);
                }
            }
        });
    }


    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getBasicEventEnum() == BasicEventEnum.USER_INFO) {
                    tvMyInfoNickname.setText(UserService.getInstance().getLoginUser().getNickName());
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final QiNiuEvent.UploadCompleteEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageUtils.displayImage(UserInfoActivity.this, new File(event.getLocalPath()), ivMyInfoHead);
                UserService.getInstance().getLoginUser().setAvatar(event.getQiniuPath());
                UserService.getInstance().userInfo("", event.getQiniuPath());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantValue.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    IntentUtils.intent2CropActivity(this, mSelectPath.get(0), ConstantValue.CROP_TYPE_HEAD);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlyt_myinfo_nickname:
                IntentUtils.intent2EditTextActivity(UserInfoActivity.this);
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
