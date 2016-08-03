package com.codingfeel.sm.ui.post;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.CircleImageView;
import com.codingfeel.sm.views.DrawableCenterTextView;
import com.codingfeel.sm.views.popup.PopupShare;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/17.
 */
public class PostDetailActivity extends ToolBarActivity implements View.OnClickListener {

    @BindView(R.id.iv_post_detail_img)
    ImageView ivPostDetailImg;
    @BindView(R.id.tv_post_detail_websiteName)
    TextView tvPostDetailWebsiteName;
    @BindView(R.id.tv_post_detail_time)
    TextView tvPostDetailTime;
    @BindView(R.id.tv_post_detail_title)
    TextView tvPostDetailTitle;
    @BindView(R.id.iv_post_detail_head)
    CircleImageView ivPostDetailHead;
    @BindView(R.id.tv_post_detail_nickname)
    TextView tvPostDetailNickname;
    @BindView(R.id.tv_post_detail_content)
    WebView tvPostDetailContent;
    @BindView(R.id.iv_post_detail_good)
    ImageView ivPostDetailGood;
    @BindView(R.id.tv_post_detail_good)
    DrawableCenterTextView tvPostDetailGood;
    @BindView(R.id.llyt_postdetail_bottom_good)
    LinearLayout llytPostdetailBottomGood;
    @BindView(R.id.tv_post_detail_comment)
    DrawableCenterTextView tvPostDetailComment;
    @BindView(R.id.llyt_postdetail_bottom_comment)
    LinearLayout llytPostdetailBottomComment;
    @BindView(R.id.tv_post_detail_share)
    DrawableCenterTextView tvPostDetailShare;
    @BindView(R.id.llyt_postdetail_bottom_share)
    LinearLayout llytPostdetailBottomShare;
    @BindView(R.id.tv_post_detail_fav)
    DrawableCenterTextView tvPostDetailFav;
    @BindView(R.id.llyt_postdetail_bottom_fav)
    LinearLayout llytPostdetailBottomFav;
    @BindView(R.id.llyt_post_detail_bottom)
    LinearLayout llytPostDetailBottom;
    @BindView(R.id.iv_post_detail_fav)
    ImageView ivPostDetailFav;

    private PostModel postModel;

    private PopupShare popupShare;

    private String postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initWebView();
        postModel = (PostModel) getIntent().getExtras().get(ContentKey.POST_MODEL);
        showToolBar(PostService.getInstance().getPostType(this, postModel.getType()) + "详情", true, null);
        if (postModel != null) {
            showEvaView(postModel.isEva());
            showFavView(postModel.isFav());
            initData(postModel);
        } else {
            postId = (String) getIntent().getExtras().get(ContentKey.INFO_ID);
            PostService.getInstance().detail(postId);
        }
        initListener();

    }


    private void initListener() {
        llytPostdetailBottomGood.setOnClickListener(this);
        llytPostdetailBottomComment.setOnClickListener(this);
        llytPostdetailBottomShare.setOnClickListener(this);
        llytPostdetailBottomFav.setOnClickListener(this);
    }

    private void initData(PostModel postModel) {

        if (postModel != null) {

            ImageUtils.displayImage(this, postModel.getPostImgUrl(), ivPostDetailImg, R.mipmap.post_img_normal_gray);

            tvPostDetailWebsiteName.setText(PostService.getInstance().getPostType(this, postModel.getType()) + " | ");

            tvPostDetailTime.setText(DateUtil.date2Str(postModel.getUpdateTime(), DateUtil.FORMAT_HM));

//            tvInfoDetailName.setText(infoModel.get);

            tvPostDetailTitle.setText(postModel.getTitle());

            ImageUtils.displayImage(this, postModel.getAvatar(), ivPostDetailHead);

            tvPostDetailNickname.setText(postModel.getNickName());


            String url = BuildConfig.H5SERVER + "/post.html?postId=" + postModel.getPostId() + "&app=1";

            tvPostDetailContent.loadUrl(url);


//            tvPostDetailContent.setText(Html.fromHtml(postModel.getContent()));

            tvPostDetailGood.setText(postModel.getEvaCount() + "");

            tvPostDetailComment.setText(postModel.getCommentCount() + "");

            tvPostDetailShare.setText(postModel.getShareCount() + "");


        }


    }


    private void initWebView() {
        tvPostDetailContent.getSettings().setJavaScriptEnabled(true);
        tvPostDetailContent.getSettings().setDefaultTextEncodingName("UTF-8");
        tvPostDetailContent.getSettings().setSupportZoom(false);
        tvPostDetailContent.getSettings().setDisplayZoomControls(false);
        // 缩放按钮
        tvPostDetailContent.getSettings().setBuiltInZoomControls(true);
        tvPostDetailContent.getSettings().setUseWideViewPort(true);

    }

    @Subscribe
    public void onEvent(final PostEvent.PostShareSucEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPostDetailShare.setText(postModel.getShareCount() + 1);
                postModel.setShareCount(postModel.getShareCount() + 1);
                PostService.getInstance().share(postModel.getPostId());
            }
        });
    }


    private void initFavView(boolean isFav) {
        if (isFav) {
            PostService.getInstance().updatePostModelFavCount(postModel.getPostId(), postModel.getFavCount() - 1, false);
            postModel = PostService.getInstance().loadPostModelsSync().get(postModel.getPostId());
//                            infoModel.setEva(false);
            tvPostDetailFav.setText(postModel.getFavCount() + "");
            ivPostDetailFav.setBackgroundResource(R.mipmap.fav_noraml);
        } else {
            PostService.getInstance().updatePostModelFavCount(postModel.getPostId(), postModel.getFavCount() + 1, true);
            postModel = PostService.getInstance().loadPostModelsSync().get(postModel.getPostId());
            tvPostDetailFav.setText(postModel.getFavCount() + "");
            ivPostDetailFav.setBackgroundResource(R.mipmap.fav_focus);
        }
    }


    private void showFavView(boolean isFav) {
        if (!isFav) {
            tvPostDetailFav.setText(postModel.getFavCount() + "");
            ivPostDetailFav.setBackgroundResource(R.mipmap.fav_noraml);
        } else {
            tvPostDetailFav.setText(postModel.getFavCount() + "");
            ivPostDetailFav.setBackgroundResource(R.mipmap.fav_focus);
        }
    }

    @Subscribe
    public void onEvent(final PostEvent.PostDetailEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ValidateUtil.hasError(event.getPostModel())) {
                    showToast(event.getPostModel().getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    initData(event.getPostModel());
                    initEvaView(event.getPostModel().isEva());
                    initFavView(event.getPostModel().isFav());
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final PostEvent.PostFavEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (!ValidateUtil.hasError(baseModel)) {
                    initFavView(postModel.isFav());
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getBasicEventEnum() == BasicEventEnum.POST_EVA) {
                    if (!ValidateUtil.hasError(event.getBaseModel())) {

                        initEvaView(postModel.isEva());
//                        if (postModel.isEva()) {
//                            postModel.setEva(false);
//                            tvPostDetailGood.setText(postModel.getEvaCount() - 1 + "");
//                            postModel.setEvaCount(postModel.getEvaCount() - 1);
//                            ivPostDetailGood.setBackgroundResource(R.mipmap.good_normal);
//                        } else {
//                            postModel.setEva(true);
//                            tvPostDetailGood.setText(postModel.getEvaCount() + 1 + "");
//                            postModel.setEvaCount(postModel.getEvaCount() + 1);
//                            ivPostDetailGood.setBackgroundResource(R.mipmap.good_focus);
//                        }

                    }
                }

            }
        });
    }

    @Subscribe
    public void onEvent(final PostEvent.PostCommentUpdateEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postModel = PostService.getInstance().loadPostModelsSync().get(postModel.getPostId());
                tvPostDetailComment.setText(postModel.getCommentCount() + "");
            }
        });
    }


    private void initEvaView(boolean isEva) {
        if (isEva) {
            PostService.getInstance().updatePostModelEvaCount(postModel.getPostId(), postModel.getEvaCount() - 1, false);
            postModel = PostService.getInstance().loadPostModelsSync().get(postModel.getPostId());
//                            infoModel.setEva(false);
            tvPostDetailGood.setText(postModel.getEvaCount() + "");
            ivPostDetailGood.setBackgroundResource(R.mipmap.good_normal);
        } else {
            PostService.getInstance().updatePostModelEvaCount(postModel.getPostId(), postModel.getEvaCount() + 1, true);
            postModel = PostService.getInstance().loadPostModelsSync().get(postModel.getPostId());
//                            infoModel.setEva(true);
            tvPostDetailGood.setText(postModel.getEvaCount() + "");
            ivPostDetailGood.setBackgroundResource(R.mipmap.good_focus);
        }
    }


    private void showEvaView(boolean isEva) {
        if (!isEva) {
            tvPostDetailGood.setText(postModel.getEvaCount() + "");
            ivPostDetailGood.setBackgroundResource(R.mipmap.good_normal);
        } else {
            tvPostDetailGood.setText(postModel.getEvaCount() + "");
            ivPostDetailGood.setBackgroundResource(R.mipmap.good_focus);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_postdetail_bottom_comment:
                IntentUtils.intent2PostCommentActivity(PostDetailActivity.this, postModel.getPostId(), postModel);
                break;
            case R.id.llyt_postdetail_bottom_fav:
                PostService.getInstance().fav(postModel.getPostId());
                break;
            case R.id.llyt_postdetail_bottom_good:
                PostService.getInstance().eva(postModel.getPostId());
                break;
            case R.id.llyt_postdetail_bottom_share:
                String url = BuildConfig.H5SERVER + "/post.html?postId=" + postModel.getPostId() + "&app=0";
                popupShare = new PopupShare(PostDetailActivity.this, url, postModel.getTitle(), PostService.getInstance().getPostShareContent(postModel.getContent()));
//                popupShare.showAtLocation(rlytInfoDetailContainer, Gravity.BOTTOM, 0, 0);
                popupShare.setFocusable(true);
                popupShare.setOutsideTouchable(true);
                popupShare.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                LogUtils.e(TAG, "================isshow" + popupShare.isShowing());
                break;
        }
    }
}
