package com.codingfeel.sm.ui.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.codingfeel.sm.R;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.model.UserModel;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.ValidateUtil;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/24.
 */
public class LoginActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_login), true, null);
        initListener();
    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                // TODO: 16/7/24 微信登录
//                UserService.getInstance().login("heboot", "123123123123123", EncryptUtils.md5("heboot" + MiscUtils.getIMEI(this)), MiscUtils.getIMEI(this), PushUtil.getPushClientId(this));
                // send oauth request
//                WXUtil.getInstance(LoginActivity.this).doLogin();
                break;
        }
    }

    @Subscribe
    public void onEvent(final UserEvent.UserLoginEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserModel userModel = event.getUserModel();
                if (ValidateUtil.hasError(userModel)) {
                    showToast(userModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    finish();
                }
            }
        });
    }


}
