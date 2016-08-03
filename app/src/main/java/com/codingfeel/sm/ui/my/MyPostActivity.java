package com.codingfeel.sm.ui.my;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.MyPostAdapter;
import com.codingfeel.sm.anim.MyItemAnimator;
import com.codingfeel.sm.bean.MyPostBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;
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
public class MyPostActivity extends ToolBarActivity {


    @BindView(R.id.rv_mypost)
    SuperRecyclerView rvMypost;
    @BindView(R.id.fb_mypost_add)
    FloatingActionButton fbMypostAdd;
    private MyPostAdapter myInfoAdapter;

    private int pageNo = 1;
    private boolean isLastPage = false;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        showToolBar(getResources().getString(R.string.page_title_my_post), true, null);

        initListener();
        initRecyclerView();
        initData();

    }

    private void initListener() {
        fbMypostAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2NewPostActivity(MyPostActivity.this);
            }
        });
    }

    private void initRecyclerView() {
        MyItemAnimator animator = new MyItemAnimator();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMypost.setLayoutManager(linearLayoutManager);
        rvMypost.getRecyclerView().setItemAnimator(animator);


        rvMypost.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvMypost.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                pageNo = 1;
                initData();
            }
        });
        rvMypost.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (isLastPage) {
                    showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                    rvMypost.hideMoreProgress();
                    return;
                }
                pageNo = pageNo + 1;
                initData();
            }
        }, 1);
    }

    private void initData() {
        UserService.getInstance().getMyPost(pageNo, ConstantValue.PAGE_SIZE);
    }

    @Subscribe
    public void onEvent(final PostEvent.PostDelEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (baseModel.getError() != null && !TextUtils.isEmpty(baseModel.getError_text())) {
                    showToast(baseModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    String postId = event.getPostId();
                    List<PostModel> postModelList = myInfoAdapter.getList();
                    for (int i = 0; i < postModelList.size(); i++) {
                        PostModel infoModel = postModelList.get(i);
                        if (infoModel.getPostId().equals(postId)) {
//                    myInfoAdapter.removeData(i);
                            LogUtils.e(TAG, "DO DEL 1==");
                            myInfoAdapter.getList().remove(i);
                            myInfoAdapter.notifyItemRemoved(i);
                            if (myInfoAdapter.getItemCount() == 0) {
                                myInfoAdapter = null;
                                rvMypost.setAdapter(null);
                            } else if (i != myInfoAdapter.getItemCount()) {
                                myInfoAdapter.notifyItemRangeChanged(i, myInfoAdapter.getItemCount());
                            }
                            return;
                        }
                    }
                }


            }
        });
    }

    @Subscribe
    public void onEvent(final UserEvent.UserMyPostEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                MyPostBean myInfoBean = event.getMyPostBean();
                if (ValidateUtil.hasError(myInfoBean)) {
                    showToast(myInfoBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {

                    if (myInfoBean.getPosts() == null || myInfoBean.getPosts().size() <= 0) {
//                            tvSearchResultNullTips.setVisibility(View.VISIBLE);
                        rvMypost.setAdapter(null);
                        return;
                    }


                    if (myInfoAdapter == null) {
                        myInfoAdapter = new MyPostAdapter(MyPostActivity.this, myInfoBean.getPosts());
                    } else {
                        if (pageNo == 1) {
                            myInfoAdapter.getList().clear();
                        }
                        myInfoAdapter.getList().addAll(myInfoBean.getPosts());

                    }
                    pageNo = pageNo + 1;
                    rvMypost.setAdapter(myInfoAdapter);
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
