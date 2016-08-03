package com.codingfeel.sm.ui.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.InfoHomeAdapter;
import com.codingfeel.sm.adapter.PostHomeAdapter;
import com.codingfeel.sm.bean.InfoHomeBean;
import com.codingfeel.sm.bean.PostHomeBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.ui.BaseHideActivity;
import com.codingfeel.sm.views.GoogleIconFontTextView;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/25.
 */
public class SearchResultActivity extends BaseHideActivity {


    @BindView(R.id.tv_searchresult_back)
    GoogleIconFontTextView tvSearchresultBack;
    @BindView(R.id.et_searchresult)
    EditText etSearchresult;
    @BindView(R.id.rv_searchresult)
    SuperRecyclerView rvSearchresult;

    private String searchType;

    private boolean isLastPage = false;

    private boolean isRunning = false;

    private int pageNo = 1;

    private InfoHomeAdapter<InfoModel> infoHomeAdapter;

    private PostHomeAdapter<PostModel> postHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initRecyclerView();
        initData();
        initListener();
    }

    private void initData() {
        searchType = getIntent().getExtras().getString(ContentKey.SEARCH_TYPE);
        rvSearchresult.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearchresult.setLayoutManager(linearLayoutManager);
    }

    private void initListener() {
        etSearchresult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchKey = charSequence.toString().trim();
                if (!TextUtils.isEmpty(searchKey)) {
                    pageNo = 1;

                    if (searchType.equals(ConstantValue.SEARCH_TYPE_INFO)) {
                        PostService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, searchKey,ConstantValue.HOME_HOT);
                    } else if (searchType.equals(ConstantValue.SEARCH_TYPE_POST)) {
                        PostService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, searchKey,ConstantValue.HOME_HOT);
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Subscribe
    public void onEvent(final PostEvent.PostSearchEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRunning = false;
                rvSearchresult.setVisibility(View.VISIBLE);
                PostHomeBean postHomeBean = event.getPostHomeBean();
                isLastPage = postHomeBean.isLastPage();
                if (postHomeBean.getPost() == null || postHomeBean.getPost().size() <= 0) {
                    rvSearchresult.hideProgress();
                    rvSearchresult.setAdapter(null);
                    return;
                }


                if (postHomeBean.getError() != null && !TextUtils.isEmpty(postHomeBean.getError_text())) {
                    showToast(postHomeBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {

                    if (postHomeBean.getPost() == null || postHomeBean.getPost().size() <= 0) {
                        if (isLastPage) {
                            showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                            rvSearchresult.hideMoreProgress();
                            return;
                        } else {
                            rvSearchresult.setAdapter(null);
                        }
                        return;
                    }


                    if (postHomeAdapter == null) {
                        postHomeAdapter = new PostHomeAdapter<PostModel>(SearchResultActivity.this, postHomeBean.getPost());
                    } else {
                        if (pageNo == 1) {
                            postHomeAdapter.getList().clear();
                        }
                        postHomeAdapter.getList().addAll(postHomeBean.getPost());

                    }
                    pageNo = pageNo + 1;
                    rvSearchresult.setAdapter(postHomeAdapter);
                    postHomeAdapter.notifyDataSetChanged();

                }
            }
        });
    }


    @Subscribe
    public void onEvent(final InfoEvent.InfoSearchEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rvSearchresult.hideMoreProgress();
                isRunning = false;
                rvSearchresult.setVisibility(View.VISIBLE);
                InfoHomeBean postHomeBean = event.getInfoHomeBean();
                if (postHomeBean.getInfo() == null || postHomeBean.getInfo().size() <= 0) {
                    rvSearchresult.hideProgress();
                    rvSearchresult.setAdapter(null);
                    return;
                }
                isLastPage = postHomeBean.isLastPage();

                if (postHomeBean.getError() != null && !TextUtils.isEmpty(postHomeBean.getError_text())) {
                    showToast(postHomeBean.getError_text(), SuperToast.Duration.VERY_SHORT);
                } else {

                    if (postHomeBean.getInfo() == null || postHomeBean.getInfo().size() <= 0) {
                        if (isLastPage) {
                            showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
                            rvSearchresult.hideMoreProgress();
                            return;
                        }
                        return;
                    }

                    List<InfoModel> infoModels = postHomeBean.getInfo();

                    if (infoHomeAdapter == null) {
//                            infoModels.add(0, new InfoModel());
                        infoHomeAdapter = new InfoHomeAdapter(SearchResultActivity.this, infoModels);
                    } else {
                        if (pageNo == 1) {
//                                infoModels.add(0,new InfoModel());
                            infoHomeAdapter.getList().clear();
                        }
                        infoHomeAdapter.getList().addAll(postHomeBean.getInfo());

                    }
                    pageNo = pageNo + 1;
                    rvSearchresult.setAdapter(infoHomeAdapter);
                    infoHomeAdapter.notifyDataSetChanged();


                }
            }
        });
    }


}
