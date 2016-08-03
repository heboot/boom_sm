package com.codingfeel.sm.ui.info;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.MyInfoAdapter;
import com.codingfeel.sm.anim.MyItemAnimator;
import com.codingfeel.sm.bean.MyInfoBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.superrecyclerview.OnMoreListener;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/13.
 */
public class MyInfoActivity extends ToolBarActivity {


    @BindView(R.id.rv_myinfo)
    SuperRecyclerView rvMyinfo;
    @BindView(R.id.fb_myinfo_add)
    FloatingActionButton fbMyinfoAdd;

    private MyInfoAdapter myInfoAdapter;

    private int pageNo = 1;
    private boolean isLastPage = false;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        showToolBar(getResources().getString(R.string.page_title_my_info), true, null);

        initListener();
        initRecyclerView();
        initData();

    }

    private void initListener() {
        fbMyinfoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2NewInfoActivity(MyInfoActivity.this);
            }
        });
    }

    private void initRecyclerView() {
        MyItemAnimator animator = new MyItemAnimator();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMyinfo.setLayoutManager(linearLayoutManager);
        rvMyinfo.getRecyclerView().setItemAnimator(animator);


        rvMyinfo.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvMyinfo.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                pageNo = 1;
                initData();
            }
        });
        rvMyinfo.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (isLastPage) {
                    showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                    rvMyinfo.hideMoreProgress();
                    return;
                }
                pageNo = pageNo + 1;
                UserService.getInstance().getMyInfo(pageNo, ConstantValue.PAGE_SIZE);
            }
        }, 1);

    }

    private void initData() {
        UserService.getInstance().getMyInfo(pageNo, ConstantValue.PAGE_SIZE);
    }

    @Subscribe
    public void onEvent(final InfoEvent.InfoDelEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (baseModel.getError() != null && !TextUtils.isEmpty(baseModel.getError_text())) {
//            context.showToast(baseModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    Message message = new Message();
                    message.obj = event.getInfoId();
                    handler.sendMessage(message);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String infoId = (String) msg.obj;
            List<InfoModel> infoModelList = myInfoAdapter.getList();
            for (int i = 0; i < infoModelList.size(); i++) {
                InfoModel infoModel = infoModelList.get(i);
                if (infoModel.getInfoId().equals(infoId)) {
//                    myInfoAdapter.removeData(i);
                    LogUtils.e(TAG, "DO DEL 1==");
                    myInfoAdapter.getList().remove(i);
                    myInfoAdapter.notifyItemRemoved(i);
                    if (myInfoAdapter.getItemCount() == 0) {
                        myInfoAdapter = null;
                        rvMyinfo.setAdapter(null);
                    } else if (i != myInfoAdapter.getItemCount()) {
                        myInfoAdapter.notifyItemRangeChanged(i, myInfoAdapter.getItemCount());
                    }
                    return;
                }
            }
        }
    };


    @Subscribe
    public void onEvent(final UserEvent.UserMyInfoEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                MyInfoBean myInfoBean = event.getMyInfoBean();
                isLastPage = myInfoBean.isLastPage();
                if (ValidateUtil.hasError(myInfoBean)) {
                    showToast(myInfoBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {

                    if (myInfoBean.getInfo() == null || myInfoBean.getInfo().size() <= 0) {
//                            tvSearchResultNullTips.setVisibility(View.VISIBLE);
                        rvMyinfo.setAdapter(null);
                        return;
                    }


                    if (myInfoAdapter == null) {
                        myInfoAdapter = new MyInfoAdapter(MyInfoActivity.this, myInfoBean.getInfo());
                    } else {
                        if (pageNo == 1) {
                            myInfoAdapter.getList().clear();
                        }
                        myInfoAdapter.getList().addAll(myInfoBean.getInfo());

                    }
                    pageNo = pageNo + 1;
                    rvMyinfo.setAdapter(myInfoAdapter);
                    myInfoAdapter.notifyDataSetChanged();


                }


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
