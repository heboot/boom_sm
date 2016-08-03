package com.codingfeel.sm.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.InputMethodUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/29.
 */
public class EditTextActivity extends ToolBarActivity implements View.OnClickListener {
    @BindView(R.id.et_edittext_content)
    EditText etEdittextContent;
    @BindView(R.id.tv_edittext_hint)
    TextView tvEdittextHint;

    String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        ButterKnife.bind(this);
        showToolBar(getResources().getString(R.string.page_title_my_nickname), true, null);
        showToolBarSend(this);
        EventBus.getDefault().register(this);

        etEdittextContent.setText(UserService.getInstance().getLoginUser().getNickName());
        etEdittextContent.setSelection(etEdittextContent.getText().length());

        InputMethodUtils.openSoftKeyboard(this, etEdittextContent);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_send:

                String content = etEdittextContent.getText().toString();
                nickName = content;
                if (TextUtils.isEmpty(content)) {
                    showToast("请输入名字", SuperToast.Duration.VERY_SHORT);
                    return;

                }
                UserService.getInstance().userInfo(content, "");
                break;
        }
    }


    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getBasicEventEnum() == BasicEventEnum.USER_INFO) {
                    BaseModel baseModel = event.getBaseModel();
                    if (!ValidateUtil.hasError(baseModel)) {
                        UserService.getInstance().getLoginUser().setNickName(nickName);
                        finish();
                    }
                }
            }
        });
    }


}
