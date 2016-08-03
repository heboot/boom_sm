package com.codingfeel.sm.ui.my;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.MyFavAdapter;
import com.codingfeel.sm.anim.MyItemAnimator;
import com.codingfeel.sm.bean.MyFavBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.ToolBarActivity;
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
 * Created by Heboot on 16/7/28.
 */
public class MyFavActivity extends ToolBarActivity {
    @BindView(R.id.rv_myfav)
    SuperRecyclerView rvMyfav;
    private MyFavAdapter myInfoAdapter;

    private int pageNo = 1;
    private boolean isLastPage = false;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        showToolBar(getResources().getString(R.string.page_title_my_fav), true, null);

        initListener();
        initRecyclerView();
        initData();

    }

    private void initListener() {
    }

    private void initRecyclerView() {
        MyItemAnimator animator = new MyItemAnimator();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMyfav.setLayoutManager(linearLayoutManager);
        rvMyfav.getRecyclerView().setItemAnimator(animator);
        rvMyfav.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvMyfav.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                pageNo = 1;
                initData();
            }
        });
        rvMyfav.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (isLastPage) {
                    showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                    rvMyfav.hideMoreProgress();
                    return;
                }
                pageNo = pageNo + 1;
                initData();
            }
        }, 1);

    }

    private void initData() {
        UserService.getInstance().getMyFav(pageNo, ConstantValue.PAGE_SIZE);
    }


    @Subscribe
    public void onEvent(final PostEvent.PostFavEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (!ValidateUtil.hasError(baseModel)) {

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
                                rvMyfav.setAdapter(null);
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
    public void onEvent(final UserEvent.UserMyFavEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                MyFavBean myInfoBean = event.getMyFavBean();
                if (ValidateUtil.hasError(myInfoBean)) {
                    showToast(myInfoBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {

                    if (myInfoBean.getFav() == null || myInfoBean.getFav().size() <= 0) {
//                            tvSearchResultNullTips.setVisibility(View.VISIBLE);
                        rvMyfav.setAdapter(null);
                        return;
                    }


                    if (myInfoAdapter == null) {
                        myInfoAdapter = new MyFavAdapter(MyFavActivity.this, myInfoBean.getFav());
                    } else {
                        if (pageNo == 1) {
                            myInfoAdapter.getList().clear();
                        }
                        myInfoAdapter.getList().addAll(myInfoBean.getFav());

                    }
                    pageNo = pageNo + 1;
                    rvMyfav.setAdapter(myInfoAdapter);
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
