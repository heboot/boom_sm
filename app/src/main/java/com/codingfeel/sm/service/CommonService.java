package com.codingfeel.sm.service;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.api.ApiClient;
import com.codingfeel.sm.api.ApiRequest;
import com.codingfeel.sm.bean.CommonBean;
import com.codingfeel.sm.bean.CommonGuestBean;
import com.codingfeel.sm.bean.MessageBean;
import com.codingfeel.sm.common.SharePrefKey;
import com.codingfeel.sm.enums.MessageLocalType;
import com.codingfeel.sm.event.MessageEvent;
import com.codingfeel.sm.event.SystemEvent;
import com.codingfeel.sm.http.HttpRequest;
import com.codingfeel.sm.http.HttpResponse;
import com.codingfeel.sm.http.HttpUtils;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.MessageModel;
import com.codingfeel.sm.model.PostTagModel;
import com.codingfeel.sm.utils.DBUtil;
import com.codingfeel.sm.utils.SharePrefUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Heboot on 16/7/18.
 */
public class CommonService extends HttpService {


    private static CommonService commonService;

    public static CommonService getInstance() {
        if (commonService == null) {
            commonService = new CommonService();
        }
        return commonService;
    }

    public void home() {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_COMMON_HOME, HttpRequest.Method.GET, false);
        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                CommonBean commonBean = JSON.parseObject(result, CommonBean.class);
                if (!TextUtils.isEmpty(commonBean.getUploadToken())) {
                    MyApplication.getInstance().setUploadToken(commonBean.getUploadToken());
                    MyApplication.getInstance().setUploadTokenUpdateTime(System.currentTimeMillis());
                    List<PostTagModel> postTagModelList = JSON.parseArray(commonBean.getPostTags(), PostTagModel.class);
                    MyApplication.getInstance().setPostTagModelList(postTagModelList);
                    MyApplication.getInstance().setQiniuPrefix(commonBean.getQiniuPrefix());
                }

            }
        });
    }

    public Observable<CommonGuestBean> homeGuest() {
        ApiRequest apiRequest = new ApiRequest(BuildConfig.HTTP_SERVER + ACTION_COMMON_HOME_GUEST, HttpRequest.Method.GET, true);
        return ApiClient.getCommonServiceInterface(apiRequest).homeGuest();
    }

    public void homeGuest(final Context context) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_COMMON_HOME_GUEST, HttpRequest.Method.GET, true);
        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                CommonGuestBean commonBean = JSON.parseObject(result, CommonGuestBean.class);
//                if (!TextUtils.isEmpty(commonBean.getAppVersion())) {
//                    int versionCode = VerisonUtil.getVersion(context);
//                    int serverVersionCode = Integer.parseInt(commonBean.getAppVersion().replace(".", ""));
//                    if (versionCode < serverVersionCode) {
//                        EventBus.getDefault().post(new SystemEvent.VerisonUpdateEvent(commonBean));
//                    } else {
                EventBus.getDefault().post(new SystemEvent.VerisonUpdateEvent(commonBean));
//                    }
//                }

            }
        });
    }


    public void delMessage(final String messageId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_COMMON_MESSAGE, HttpRequest.Method.PUT, true);
        httpRequest.addParams(PARAM_MESSAGE_ID, messageId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_MESSAGE_ID, String.valueOf(messageId))
                .build();
        httpRequest.setFormBody(formBody);


        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new MessageEvent.MessageDelEvent(baseModel, messageId));

            }
        });
    }


    public void getMessage() {
        long messageTime = SharePrefUtil.getLong(MyApplication.getInstance(), SharePrefKey.PREFERENCES_GET_MESSAGE_TIME, 0);
        long messageSystemTime = SharePrefUtil.getLong(MyApplication.getInstance(), SharePrefKey.PREFERENCES_GET_MESSAGE_SYSTEM_TIME, 0);
        if (messageTime == 0) {
            messageTime = 1469203200;
        }
        if (messageSystemTime == 0) {
            messageSystemTime = 1469203200;
        }
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_COMMON_MESSAGE, HttpRequest.Method.GET, false);
        httpRequest.addParams(PARAM_MESSAGE_CREATE_TIME, String.valueOf(messageTime));
        httpRequest.addParams(PARAM_MESSAGE_SYSTEM_CREATE_TIME, String.valueOf(messageSystemTime));

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_MESSAGE_CREATE_TIME, String.valueOf(messageTime))
                .add(PARAM_MESSAGE_SYSTEM_CREATE_TIME, String.valueOf(messageSystemTime))
                .build();
        httpRequest.setFormBody(formBody);


        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                MessageBean messageBean = JSON.parseObject(result, MessageBean.class);
                if (messageBean.getSystemMessages() != null && messageBean.getSystemMessages().size() > 0) {
                    SharePrefUtil.saveLong(MyApplication.getInstance(), SharePrefKey.PREFERENCES_GET_MESSAGE_SYSTEM_TIME, messageBean.getSystemMessages().get(0).getCreateTime().getTime());
                    for (MessageModel messageModel : messageBean.getSystemMessages()) {
                        messageModel.setLocalType(MessageLocalType.SYSTEM.getValue());
                        DBUtil.getInstance().saveMessage(messageModel);
                    }
                }
                if (messageBean.getUserMessages() != null && messageBean.getUserMessages().size() > 0) {
                    SharePrefUtil.saveLong(MyApplication.getInstance(), SharePrefKey.PREFERENCES_GET_MESSAGE_TIME, messageBean.getUserMessages().get(0).getCreateTime().getTime());
                    for (MessageModel messageModel : messageBean.getUserMessages()) {
                        DBUtil.getInstance().saveMessage(messageModel);
                    }
                }
                EventBus.getDefault().post(new MessageEvent.MessageRefreshEvent());
            }
        });
    }


}
