package com.codingfeel.sm.ui.post;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.PostHomeAdapter2;
import com.codingfeel.sm.bean.PostHomeBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.event.UIEvent;
import com.codingfeel.sm.listener.HidingScrollListener;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.BaseFragment;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.views.GoogleIconFontTextView;
import com.codingfeel.sm.views.superrecyclerview.OnMoreListener;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;
import com.codingfeel.sm.views.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/12.
 */
public class PostHotFragment extends BaseFragment {


    @BindView(R.id.rv_post)
    SuperRecyclerView rvPost;
    @BindView(R.id.tv_setting)
    GoogleIconFontTextView tvSetting;
    @BindView(R.id.et_post_search)
    TextView etPostSearch;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.llyt_post_search)
    LinearLayout llytPostSearch;

    private String keywords = "";

    private int pageNo = 1;
    private boolean isLastPage = false;

    private boolean isRunning = false;

    private boolean isSearch = false;

    private PostHomeAdapter2<PostModel> postHomeAdapter;
    AnimatorSet animatorSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initRecyclerView();
        initData();
        initListener();
        initAnimation();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void initAnimation() {
        animatorSet = new AnimatorSet();
        ObjectAnimator yobjectAnimator = ObjectAnimator.ofFloat(tvAdd, "scaleY", 1, 1.3f, 1);
        yobjectAnimator.setRepeatCount(1000);
        ObjectAnimator xobjectAnimator = ObjectAnimator.ofFloat(tvAdd, "scaleX", 1, 1.3f, 1);
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


    private void initData() {
        PostService.getInstance().home(ConstantValue.PAGE_SIZE, ConstantValue.PAGE_NO, keywords, ConstantValue.HOME_HOT);
    }

    private void initListener() {
        etPostSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.intent2SearchResultActivity(getContext(), ConstantValue.SEARCH_TYPE_POST);
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!UserService.getInstance().isLogin()) {
                    IntentUtils.intent2LoginActivity(getContext());
                    return;
                }
                IntentUtils.intent2NewPostActivity(getContext());
            }
        });
        rvPost.setOnScrollListener(new HidingScrollListener() {
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

    private void hideViews() {
        llytPostSearch.animate().translationY(-llytPostSearch.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        llytPostSearch.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPost.setLayoutManager(linearLayoutManager);


        rvPost.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvPost.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                pageNo = 1;
                PostService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, keywords, ConstantValue.HOME_HOT);
            }
        });
        rvPost.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
//                showToast("onMoreAsked",SuperToast.Duration.VERY_SHORT);
                LogUtils.e(TAG, itemsBeforeMore + "onMoreAsked" + overallItemsCount + "====" + maxLastVisiblePosition);
                if (isLastPage) {
                    rvPost.hideMoreProgress();
                    return;
                }
                pageNo = pageNo + 1;
                PostService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, keywords, ConstantValue.HOME_HOT);
            }
        }, 1);
        rvPost.setupSwipeToDismiss(new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                LogUtils.e(TAG, "onDismiss");
            }
        });
        rvPost.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

    }


    @Subscribe
    public void onEvent(final PostEvent.PostHomeEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;

                PostHomeBean postHomeBean = event.getPostHomeBean();
                isLastPage = postHomeBean.isLastPage();


                if (postHomeBean.getPost() == null || postHomeBean.getPost().size() <= 0) {
                    rvPost.hideMoreProgress();
                    return;
                }
                List<PostModel> postModels = postHomeBean.getPost();

                if (postHomeAdapter == null) {
                    postModels.add(0, new PostModel());
                    postHomeAdapter = new PostHomeAdapter2<PostModel>(getActivity(), postModels);
                } else {
                    if (pageNo == 1) {
                        postModels.add(0, new PostModel());
                        postHomeAdapter.getList().clear();
                    }
                    postHomeAdapter.getList().addAll(postModels);

                }

                for (PostModel postModel : postHomeBean.getPost()) {
                    PostService.getInstance().savePostModel(postModel);
                }

                pageNo = pageNo + 1;
                rvPost.setAdapter(postHomeAdapter);
                postHomeAdapter.notifyDataSetChanged();
            }


        });
    }

}
