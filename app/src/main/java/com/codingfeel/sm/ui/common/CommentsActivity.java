package com.codingfeel.sm.ui.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.CommentByInfoAdapter;
import com.codingfeel.sm.adapter.CommentByPostAdapter;
import com.codingfeel.sm.bean.InfoCommentBean;
import com.codingfeel.sm.bean.PostCommentBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.InfoCommentModel;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.model.PostCommentModel;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.InputMethodUtils;
import com.codingfeel.sm.views.GoogleIconFontTextView;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/14.
 */
public class CommentsActivity extends ToolBarActivity implements View.OnClickListener {


    @BindView(R.id.rv_comments)
    SuperRecyclerView rvComments;
    @BindView(R.id.et_comment_content)
    EditText etCommentContent;
    @BindView(R.id.tv_comment_send)
    GoogleIconFontTextView tvCommentSend;
    @BindView(R.id.llyt_comment_bottom)
    LinearLayout llytCommentBottom;

    private String pageJump;

    private String infoId, postId;

    private int pageNo = 1;
    private boolean isLastPage = false;

    private boolean isRunning = false;

    private CommentByInfoAdapter<InfoCommentModel> commentByInfoAdapter;
    private CommentByPostAdapter<PostCommentModel> commentByPostAdapter;

    private View.OnClickListener infoOnclick;

    private Integer reUid;

    private InfoModel infoModel;
    private PostModel postModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_comment), true, null);
        initRecyclerView();
        initIntentData();
        initData();
        initListener();
    }

    private void initListener() {
        tvCommentSend.setOnClickListener(this);
        infoOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }


    public void setReUid(Integer uid) {
        reUid = uid;
    }

    public void setReName(String reName) {
        etCommentContent.setHint("回复" + reName);
    }

    private void initIntentData() {
        pageJump = (String) getIntent().getExtras().get(ContentKey.PAGEJUMP_COMMENT);
    }

    private void initData() {
        if (pageJump.equals(ContentKey.PAGEJUMP_COMMENT_INFO)) {
            infoId = getIntent().getStringExtra(ContentKey.INFO_ID);
            infoModel = (InfoModel) getIntent().getExtras().get(ContentKey.INFO_MODEL);
            if (infoModel == null) {
                infoModel = InfoService.getInstance().loadInfoModelsSync().get(infoId);
            }
            InfoService.getInstance().getComments(infoId, ConstantValue.PAGE_SIZE, pageNo);
        } else if (pageJump.equals(ContentKey.PAGEJUMP_COMMENT_POST)) {
            postId = getIntent().getStringExtra(ContentKey.POST_ID);
            postModel = (PostModel) getIntent().getExtras().get(ContentKey.POST_MODEL);
            if (postModel == null) {
                postModel = PostService.getInstance().loadPostModelsSync().get(postId);
            }
            PostService.getInstance().getComments(postId, ConstantValue.PAGE_SIZE, pageNo);
        }
    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvComments.setLayoutManager(linearLayoutManager);
    }


    @Subscribe
    public void onEvent(final PostEvent.PostCommentsEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                PostCommentBean postCommentBean = event.getPostCommentBean();
                isLastPage = postCommentBean.isLastPage();
                if (postCommentBean.getError() != null && !TextUtils.isEmpty(postCommentBean.getError_text())) {
                    showToast(postCommentBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    if (postCommentBean.getComments() == null || postCommentBean.getComments().size() < 0) {
                        rvComments.setAdapter(null);
                        if (isLastPage) {
                            showToast("没有更多了", SuperToast.Duration.VERY_SHORT);
                        }
                        return;
                    }
                    if (commentByPostAdapter == null) {
                        commentByPostAdapter = new CommentByPostAdapter<PostCommentModel>(CommentsActivity.this, postCommentBean.getComments());
                    } else {
                        if (pageNo == 1) {
                            commentByPostAdapter.getList().clear();
                        }
                        commentByPostAdapter.getList().addAll(postCommentBean.getComments());
                    }
                    pageNo = pageNo + 1;
                    rvComments.setAdapter(commentByPostAdapter);
                    commentByPostAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final InfoEvent.InfoCommentsEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                InfoCommentBean infoCommentBean = event.getInfoCommentBean();
                isLastPage = infoCommentBean.isLastPage();
                if (infoCommentBean.getError() != null && !TextUtils.isEmpty(infoCommentBean.getError_text())) {
                    showToast(infoCommentBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {

                    if (infoCommentBean.getComments() == null || infoCommentBean.getComments().size() < 0) {
                        rvComments.setAdapter(null);
                        if (isLastPage) {
                            showToast("没有更多了", SuperToast.Duration.VERY_SHORT);
                        }
                        return;
                    }

                    if (commentByInfoAdapter == null) {
                        commentByInfoAdapter = new CommentByInfoAdapter<InfoCommentModel>(CommentsActivity.this, infoCommentBean.getComments(), CommentsActivity.this);
                    } else {
                        if (pageNo == 1) {
                            commentByInfoAdapter.getList().clear();
                        }
                        commentByInfoAdapter.getList().addAll(infoCommentBean.getComments());
                    }
                    pageNo = pageNo + 1;
                    rvComments.setAdapter(commentByInfoAdapter);
                    commentByInfoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void resetInput() {
        etCommentContent.setText("");
        InputMethodUtils.closeSoftKeyboard(this);
    }


    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (baseModel.getError() != null && !TextUtils.isEmpty(baseModel.getError_text())) {
                    showToast(baseModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {
                    resetInput();
                    if (event.getBasicEventEnum() == BasicEventEnum.INFO_COMMENT) {
                        pageNo = 1;
                        InfoService.getInstance().getComments(infoId, ConstantValue.PAGE_SIZE, pageNo);

                        if (infoModel != null) {
                            InfoService.getInstance().updateInfoModelCommentCount(infoId, infoModel.getCommentCount() + 1);
                            infoModel = InfoService.getInstance().loadInfoModelsSync().get(infoId);
                            EventBus.getDefault().post(new InfoEvent.InfoCommentUpdateEvent(infoId));
                        }
                    } else if (event.getBasicEventEnum() == BasicEventEnum.POST_COMMENT) {
                        pageNo = 1;
                        PostService.getInstance().getComments(postId, ConstantValue.PAGE_SIZE, pageNo);
                        if (postModel != null) {
                            PostService.getInstance().updatePostModelCommentCount(postId, postModel.getCommentCount() + 1);
                            postModel = PostService.getInstance().loadPostModelsSync().get(postId);
                            EventBus.getDefault().post(new PostEvent.PostCommentUpdateEvent(postId));
                        }
                    }
                }


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_comment_send:
                String content = etCommentContent.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    showToast("说点什么吧", SuperToast.Duration.VERY_SHORT);
                    return;
                }


                if (pageJump.equals(ContentKey.PAGEJUMP_COMMENT_INFO)) {
                    InfoService.getInstance().comment(infoId, content, reUid);
                } else if (pageJump.equals(ContentKey.PAGEJUMP_COMMENT_POST)) {
                    PostService.getInstance().comment(postId, content, reUid);
                }


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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
