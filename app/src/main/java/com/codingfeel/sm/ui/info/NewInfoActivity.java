package com.codingfeel.sm.ui.info;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/16.
 */
public class NewInfoActivity extends ToolBarActivity implements View.OnClickListener {


    @BindView(R.id.et_newinfo_link)
    EditText etNewinfoLink;
    @BindView(R.id.et_newinfo_info)
    EditText etNewinfoInfo;
    @BindView(R.id.et_newinfo_content)
    EditText etNewinfoContent;
    @BindView(R.id.tv_newinfo_num)
    TextView tvNewinfoNum;

    private CharSequence temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_new_info), true, null);
        showToolBarSend(this);

        initEditContent();

    }


    /**
     * 字数限制
     */
    private void initEditContent() {
        etNewinfoContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

        etNewinfoContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LogUtils.e(TAG, "==========beforeTextChanged " + charSequence.toString());
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LogUtils.e(TAG, "==========onTextChanged" + charSequence.toString());
//                etNewinfoContent.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                LogUtils.e(TAG, "==========afterTextChanged");
                tvNewinfoNum.setText(temp.length() + "/199");
                if (temp != null && temp.length() > 199) {
                    showToast("不要超过字数限制", SuperToast.Duration.VERY_SHORT);
                    return;
                }
            }
        });
    }


    private void sendInfo() {
        String title = etNewinfoInfo.getText().toString();
        String link = etNewinfoLink.getText().toString();
        String content = etNewinfoContent.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showToast("记得写优惠信息", SuperToast.Duration.VERY_SHORT);
            return;
        }

        if (TextUtils.isEmpty(link)) {
            showToast("记得写链接", SuperToast.Duration.VERY_SHORT);
            return;
        }

        if (TextUtils.isEmpty(content)) {
            showToast("记得写内容", SuperToast.Duration.VERY_SHORT);
            return;
        }

        InfoService.getInstance().info(title, content, link);
    }

    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (event.getBasicEventEnum() == BasicEventEnum.INFO_INFO) {
                    if (ValidateUtil.hasError(baseModel)) {
                        showToast(baseModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                    } else {
                        showToast("发布成功", SuperToast.Duration.VERY_SHORT);
                        finish();
                    }
                }


            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_send:
                sendInfo();
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
