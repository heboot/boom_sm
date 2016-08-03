package com.codingfeel.sm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.common.SharePrefKey;
import com.codingfeel.sm.enums.MessageLocalType;
import com.codingfeel.sm.event.MessageEvent;
import com.codingfeel.sm.model.MessageModel;
import com.codingfeel.sm.ui.common.CommentsActivity;
import com.codingfeel.sm.ui.info.MyInfoActivity;
import com.codingfeel.sm.ui.message.SystemMessageActivity;
import com.codingfeel.sm.ui.my.MyPostActivity;
import com.codingfeel.sm.utils.DBUtil;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.NotificationUtil;
import com.codingfeel.sm.utils.SharePrefUtil;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Heboot on 16/2/17.
 */
public class GetuiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            switch (bundle.getInt(PushConsts.CMD_ACTION)) {
                //获取透传数据
                case PushConsts.GET_MSG_DATA:
                    // 获取透传数据
                    // String appid = bundle.getString("appid");
                    byte[] payload = bundle.getByteArray("payload");

                    String taskid = bundle.getString("taskid");
                    String messageid = bundle.getString("messageid");

                    // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                    boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                    System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));


                    if (payload != null) {
                        String data = new String(payload);
                        LogUtils.e("========GETUI", "========GETUI" + data);
                        if (!TextUtils.isEmpty(data)) {
                            MessageModel messageModel = JSONObject.parseObject(data, MessageModel.class);


                            Intent pendingIntent = new Intent();
                            switch (messageModel.getType()) {
                                case ConstantValue.MESSAGE_TYPE_COMMENT_POST:
                                case ConstantValue.MESSAGE_TYPE_COMMENT_POST_RE:
                                    pendingIntent.setClass(context, CommentsActivity.class);
                                    pendingIntent.putExtra(ContentKey.PAGEJUMP_COMMENT, ContentKey.PAGEJUMP_COMMENT_POST);
                                    pendingIntent.putExtra(ContentKey.POST_ID, messageModel.getPostId());
                                    break;
                                case ConstantValue.MESSAGE_TYPE_COMMENT_INFO:
                                case ConstantValue.MESSAGE_TYPE_COMMENT_INFO_RE:
                                    pendingIntent.setClass(context, CommentsActivity.class);
                                    pendingIntent.putExtra(ContentKey.PAGEJUMP_COMMENT, ContentKey.PAGEJUMP_COMMENT_INFO);
                                    pendingIntent.putExtra(ContentKey.INFO_ID, messageModel.getInfoId());
                                    break;
                                case ConstantValue.MESSAGE_TYPE_INFO_VERIFY:
                                    pendingIntent.setClass(context, MyInfoActivity.class);
                                    break;
                                case ConstantValue.MESSAGE_TYPE_POST_VERIFY:
                                    pendingIntent.setClass(context, MyPostActivity.class);
                                    break;
                                case ConstantValue.MESSAGE_TYPE_SYSTEM_WEB:
                                    pendingIntent.setClass(context, SystemMessageActivity.class);
                                    break;
                                case ConstantValue.MESSAGE_TYPE_SYSTEM_INFO:
                                case ConstantValue.MESSAGE_TYPE_SYSTEM_POST:
                                    messageModel.setLocalType(MessageLocalType.SYSTEM.getValue());
                                    break;
                            }


                            DBUtil.getInstance().saveMessage(messageModel);
                            SharePrefUtil.saveLong(context, SharePrefKey.PREFERENCES_GET_MESSAGE_TIME, messageModel.getCreateTime() == null ? System.currentTimeMillis() : messageModel.getCreateTime().getTime());
                            if (messageid != null) {


                                EventBus.getDefault().post(new MessageEvent.NewMessageEvent(messageModel));
                                NotificationUtil.messageNotify(messageModel.getMessageTitle(), messageModel.getMessageContent(), pendingIntent);
//                                if(pushModel.getType().toUpperCase().equals(ConstantValue.PushType.WEB.toString())){
//                                    pendingIntent.setClass(context, HtmlActivity.class);
//                                    pendingIntent.putExtra(ContentKey.WEB_URL, pushModel.getUrl());
//
//                                    NotificationUtil.showSimpleNotify(context, pushModel.getTitle(), pushModel.getMessage(), pendingIntent);
//                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
}
