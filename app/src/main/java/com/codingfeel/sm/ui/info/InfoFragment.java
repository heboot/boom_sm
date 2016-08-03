package com.codingfeel.sm.ui.info;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.InfoHomeAdapter2;
import com.codingfeel.sm.bean.InfoHomeBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.event.UIEvent;
import com.codingfeel.sm.listener.HidingScrollListener;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.BaseFragment;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.views.GoogleIconFontTextView;
import com.codingfeel.sm.views.superrecyclerview.OnMoreListener;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;
import com.codingfeel.sm.views.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/11.
 */
public class InfoFragment extends BaseFragment {

    @BindView(R.id.rv_info)
    SuperRecyclerView rvInfo;
    @BindView(R.id.tv_setting)
    GoogleIconFontTextView tvSetting;
    @BindView(R.id.et_info_search)
    TextView etInfoSearch;
    @BindView(R.id.tv_add)
    GoogleIconFontTextView tvAdd;
    @BindView(R.id.llyt_info_search)
    LinearLayout llytInfoSearch;

    private String keywords = "";

    private int pageNo = 1;

    private boolean isLastPage = false;

    private boolean isRunning = false;

    private boolean isSearch = false;

    private InfoHomeAdapter2 infoHomeAdapter;

    private LinearLayoutManager layoutManager;

    private boolean searchIsShow = false;

    private int disy;
    AnimatorSet animatorSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initRecyclerView();
        initData();
        initListener();
        initAnimation();
        return view;
    }

    private void initAnimation() {
        animatorSet = new AnimatorSet();
        com.nineoldandroids.animation.ObjectAnimator yobjectAnimator = com.nineoldandroids.animation.ObjectAnimator.ofFloat(tvAdd, "scaleY", 1, 1.3f, 1);
        yobjectAnimator.setRepeatCount(1000);
        com.nineoldandroids.animation.ObjectAnimator xobjectAnimator = com.nineoldandroids.animation.ObjectAnimator.ofFloat(tvAdd, "scaleX", 1, 1.3f, 1);
        xobjectAnimator.setRepeatCount(1000);
        animatorSet.playTogether(yobjectAnimator, xobjectAnimator);
        animatorSet.setDuration(4000);
        animatorSet.addListener(listener);
        animatorSet.start();
    }

    Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
//            animatorSet.setStartDelay(1000);
//            animatorSet.addListener(listener);
//            animatorSet.setDuration(3000);
////            YoYo.w ith(Techniques.Pulse).withListener(this).delay(1000).duration(2000).playOn(tvAdd);
//            animatorSet.start();

            LogUtils.e(TAG, "onAnimationEnd");

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            LogUtils.e(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            LogUtils.e(TAG, "onAnimationRepeat");
            animatorSet.start();
        }
    };


    private void initListener() {
        etInfoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2SearchResultActivity(getActivity(), ConstantValue.SEARCH_TYPE_INFO);
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserService.getInstance().isLogin()) {
                    IntentUtils.intent2LoginActivity(getContext());
                    return;
                }
                IntentUtils.intent2NewInfoActivity(getActivity());
            }
        });
        llytInfoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2SearchResultActivity(getActivity(), ConstantValue.SEARCH_TYPE_INFO);
            }
        });

    }

    private void hideViews() {
        llytInfoSearch.animate().translationY(-llytInfoSearch.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        llytInfoSearch.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private void initData() {
        InfoService.getInstance().home(ConstantValue.PAGE_SIZE, ConstantValue.PAGE_NO, keywords);
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvInfo.setLayoutManager(layoutManager);
        rvInfo.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvInfo.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                pageNo = 1;
                InfoService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, keywords);
            }
        });
        rvInfo.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                if (isLastPage) {
                    showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                    rvInfo.hideMoreProgress();
                    return;
                }
                pageNo = pageNo + 1;
                InfoService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, keywords);
            }
        }, 1);
        rvInfo.setupSwipeToDismiss(new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                LogUtils.e(TAG, "onDismiss");
            }
        });
        rvInfo.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
                EventBus.getDefault().post(new UIEvent.MainBottomEvent(false));
            }

            @Override
            public void onShow() {
                showViews();
                EventBus.getDefault().post(new UIEvent.MainBottomEvent(true));
            }
        });


    }


    @Subscribe
    public void onEvent(final InfoEvent.InfoHomeEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rvInfo.hideMoreProgress();
                isRunning = false;

                InfoHomeBean postHomeBean = event.getInfoHomeBean();
                isLastPage = postHomeBean.isLastPage();

                if (postHomeBean.getError() != null && !TextUtils.isEmpty(postHomeBean.getError_text())) {
                    showToast(postHomeBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    if (isSearch) {
                        isSearch = false;
                        if (postHomeBean.getInfo() == null || postHomeBean.getInfo().size() <= 0) {
                            if (isLastPage) {
                                showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                                rvInfo.hideMoreProgress();
                                return;
                            }
//                            tvSearchResultNullTips.setVisibility(View.VISIBLE);
                            return;
                        }
//                        if (homeSearchResultAdapter == null) {
//                            homeSearchResultAdapter = new HomeSearchResultAdapter(MainActivity.this, postHomeBean.getPost());
//                        } else {
//                            if (homeSearchResultAdapter.getPostModels() != null) {
//                                homeSearchResultAdapter.setPostModels(postHomeBean.getPost());
//                            }
//
//                        }
//                        rvSearchResult.setAdapter(homeSearchResultAdapter);
//                        homeSearchResultAdapter.notifyDataSetChanged();
                    } else {

                        if (postHomeBean.getInfo() == null || postHomeBean.getInfo().size() <= 0) {
//                            tvSearchResultNullTips.setVisibility(View.VISIBLE);
                            return;
                        }

                        List<InfoModel> infoModels = postHomeBean.getInfo();

                        if (infoHomeAdapter == null) {
                            infoModels.add(0, new InfoModel());
                            infoHomeAdapter = new InfoHomeAdapter2(getActivity(), infoModels);
                        } else {
                            if (pageNo == 1) {
                                infoModels.add(0, new InfoModel());
                                infoHomeAdapter.getList().clear();
                            }
                            infoHomeAdapter.getList().addAll(postHomeBean.getInfo());
                        }
                        for (InfoModel infoModel : postHomeBean.getInfo()) {
                            InfoService.getInstance().saveInfoModel(infoModel);
                        }

                        pageNo = pageNo + 1;
                        rvInfo.setAdapter(infoHomeAdapter);
//                        infoHomeAdapter.notifyDataSetChanged();
                    }


                }
            }
        });
    }


}
