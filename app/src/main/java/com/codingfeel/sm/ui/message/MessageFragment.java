package com.codingfeel.sm.ui.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.adapter.MessageAdapter2;
import com.codingfeel.sm.anim.MyItemAnimator;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.event.MessageEvent;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.MessageModel;
import com.codingfeel.sm.service.CommonService;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.BaseFragment;
import com.codingfeel.sm.utils.DBUtil;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.CircleImageView;
import com.codingfeel.sm.views.superrecyclerview.SuperRecyclerView;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/12.
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_message)
    SuperRecyclerView rvMessage;
    @BindView(R.id.iv_message_system_head)
    CircleImageView ivMessageSystemHead;
    @BindView(R.id.tv_message_system_name)
    TextView tvMessageSystemName;
    @BindView(R.id.tv_message_item_time)
    TextView tvMessageItemTime;
    @BindView(R.id.tv_message_item_content)
    TextView tvMessageItemContent;

    private MessageAdapter2<MessageModel> messageAdapter;

    private MessageModel systemMessageModel;

    private int pageNo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initRecyclerView();
        initSystemMessage(null);
        initMessageList();
        initListener();
        return view;
    }

    private void initListener() {
//        cvMessageSystem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtils.intent2SystemMessageActivity(getActivity());
//            }
//        });
    }


    private void initRecyclerView() {
        MyItemAnimator animator = new MyItemAnimator();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMessage.setLayoutManager(linearLayoutManager);
        rvMessage.getRecyclerView().setItemAnimator(animator);
        rvMessage.setRefreshingColorResources(R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red, R.color.themeColor_red);
        rvMessage.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e(TAG, "onRefresh");
                pageNo = 1;
                CommonService.getInstance().getMessage();
            }
        });
//        rvMessage.setupMoreListener(new OnMoreListener() {
//            @Override
//            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
////                showToast("onMoreAsked",SuperToast.Duration.VERY_SHORT);
//                LogUtils.e(TAG, itemsBeforeMore + "onMoreAsked" + overallItemsCount + "====" + maxLastVisiblePosition);
//                if (isLastPage) {
//                    showToast(getResources().getString(R.string.lastpage_tips), SuperToast.Duration.VERY_SHORT);
//                    rvPost.hideMoreProgress();
//                    return;
//                }
//                pageNo = pageNo + 1;
//                PostService.getInstance().home(ConstantValue.PAGE_SIZE, pageNo, keywords);
//            }
//        }, 1);
//        rvMessage.setOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
//                hideViews();
//            }
//
//            @Override
//            public void onShow() {
//                showViews();
//            }
//        });
//

    }

    private void initSystemMessage(MessageModel messageModel) {
        if (!UserService.getInstance().isLogin()) {
            rvMessage.setAdapter(null);
            rvMessage.hideProgress();
            return;
        }
        if (messageModel == null) {
            systemMessageModel = DBUtil.getInstance().getLastSystemMessage(UserService.getInstance().getLoginUser().getUid());
            if (systemMessageModel == null) {
                systemMessageModel = new MessageModel();
                systemMessageModel.setMessageTitle("系统消息");
            }
        } else {
            systemMessageModel = messageModel;
        }

        if (systemMessageModel == null) {
            tvMessageItemTime.setVisibility(View.GONE);
            tvMessageItemContent.setVisibility(View.GONE);
        } else {
            tvMessageItemContent.setText(systemMessageModel.getMessageContent());
            tvMessageSystemName.setText(systemMessageModel.getMessageTitle());
            tvMessageItemTime.setText(DateUtil.date2Str(systemMessageModel.getCreateTime(), DateUtil.FORMAT_HM));
        }
    }

    private void initMessageList() {
        if (!UserService.getInstance().isLogin()) {
            return;
        }
        List<MessageModel> messageModelList = DBUtil.getInstance().getMessageList(UserService.getInstance().getLoginUser().getUid());
        messageModelList.add(0, systemMessageModel);
        if (messageModelList != null && messageModelList.size() > 0) {
            messageAdapter = new MessageAdapter2<MessageModel>(getActivity(), messageModelList);
            rvMessage.setAdapter(messageAdapter);
        }
    }


    @Subscribe
    public void onEvent(UserEvent.UserLoginEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initSystemMessage(null);
                initMessageList();
            }
        });
    }


    @Subscribe
    public void onEvent(MessageEvent.MessageRefreshEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initSystemMessage(null);
                initMessageList();
                rvMessage.hideMoreProgress();
            }
        });
    }

    @Subscribe
    public void onEvent(final MessageEvent.MessageDelEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();

                if (ValidateUtil.hasError(baseModel)) {
                    showToast(baseModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                    return;
                }

                DBUtil.getInstance().delMessage(event.getMessageId());

                List<MessageModel> messageModels = messageAdapter.getList();
                for (int i = 0; i < messageModels.size(); i++) {
                    MessageModel messageModel = messageModels.get(i);
                    if (messageModel.getUuid() != null && messageModel.getUuid().equals(event.getMessageId())) {
//                    myInfoAdapter.removeData(i);
                        LogUtils.e(TAG, "DO DEL 1==");
                        messageAdapter.getList().remove(i);
                        messageAdapter.notifyItemRemoved(i);
                        if (messageAdapter.getItemCount() == 0) {
                            messageAdapter = null;
                            rvMessage.setAdapter(null);
                        } else if (i != messageAdapter.getItemCount()) {
                            messageAdapter.notifyItemRangeChanged(i, messageAdapter.getItemCount());
                        }
                        return;
                    }
                }
            }
        });
    }


    @Subscribe
    public void onEvent(final MessageEvent.NewMessageEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageModel messageModel = event.getMessageModel();
                switch (messageModel.getType()) {
                    case ConstantValue.MESSAGE_TYPE_COMMENT_POST:
                    case ConstantValue.MESSAGE_TYPE_COMMENT_POST_RE:
                    case ConstantValue.MESSAGE_TYPE_COMMENT_INFO:
                    case ConstantValue.MESSAGE_TYPE_COMMENT_INFO_RE:
                    case ConstantValue.MESSAGE_TYPE_INFO_VERIFY:
                    case ConstantValue.MESSAGE_TYPE_POST_VERIFY:
                        if (messageAdapter != null && messageAdapter.getList() != null) {
                            messageAdapter.getList().add(1, event.getMessageModel());
                            messageAdapter.notifyItemInserted(1);
                        } else {
                            List<MessageModel> messageModels = new ArrayList<MessageModel>();
                            messageModels.add(messageModel);
                            messageAdapter = new MessageAdapter2<MessageModel>(getActivity(), messageModels);
                            rvMessage.setAdapter(messageAdapter);
                        }
                        break;
                    case ConstantValue.MESSAGE_TYPE_SYSTEM_WEB:
                    case ConstantValue.MESSAGE_TYPE_SYSTEM_INFO:
                    case ConstantValue.MESSAGE_TYPE_SYSTEM_POST:
                        initSystemMessage(messageModel);
                        break;
                }


            }
        });
    }


}
