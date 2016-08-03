package com.codingfeel.sm.ui.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.MessageAdapter;
import com.codingfeel.sm.model.MessageModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.DBUtil;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/27.
 */
public class SystemMessageActivity extends ToolBarActivity {

    @BindView(R.id.rv_message_system)
    SuperRecyclerView rvMessageSystem;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_system);
        ButterKnife.bind(this);
        showToolBar(getResources().getString(R.string.page_title_message_system), true, null);
        initRecyclerView();
        initData();

    }

    private void initData() {
        List<MessageModel> messageModelList = DBUtil.getInstance().getSystemMessageList(UserService.getInstance().getLoginUser().getUid());
        if (messageModelList != null && messageModelList.size() > 0) {
            messageAdapter = new MessageAdapter(this, messageModelList);
            rvMessageSystem.setAdapter(messageAdapter);
        } else {
            rvMessageSystem.setAdapter(null);
        }
    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMessageSystem.setLayoutManager(linearLayoutManager);

        rvMessageSystem.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvMessageSystem.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                initData();
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
