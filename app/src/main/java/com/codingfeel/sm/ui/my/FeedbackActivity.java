package com.codingfeel.sm.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codingfeel.sm.R;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.github.johnpersano.supertoasts.SuperToast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/6/29.
 */
public class FeedbackActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.et_feedback_contact)
    EditText etFeedbackContact;
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent;
    @BindView(R.id.bt_feedback_submit)
    Button btFeedbackSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);

        showToolBar(getResources().getString(R.string.page_title_feedback), true, null);


        initListener();
    }

    private void initListener() {
        btFeedbackSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_feedback_submit) {
            String content = etFeedbackContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                showToast(getResources().getString(R.string.feedback_content_null_tips), SuperToast.Duration.VERY_SHORT);
                return;
            }
//            UserService.getInstance().feedback(content, "", "", "");
        }
    }

//
//    @Subscribe
//    public void onEvent(final BaseEvent.BasicEvent event) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (event.getBasicEventEnum() == BasicEventEnum.USER_FEEDBACK) {
//                    if (event.getBaseModel().getError() != null && !TextUtils.isEmpty(event.getBaseModel().getError_text())) {
//                        showToast(event.getBaseModel().getError_text(), SuperToast.Duration.VERY_SHORT);
//                    } else {
//                        showToast(getResources().getString(R.string.feedback_success), SuperToast.Duration.VERY_SHORT);
//                        finish();
//                    }
//                }
//
//            }
//        });
//
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
