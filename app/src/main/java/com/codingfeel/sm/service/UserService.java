package com.codingfeel.sm.service;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.bean.MyFavBean;
import com.codingfeel.sm.bean.MyInfoBean;
import com.codingfeel.sm.bean.MyPostBean;
import com.codingfeel.sm.common.SharePrefKey;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.event.UserEvent;
import com.codingfeel.sm.http.HttpRequest;
import com.codingfeel.sm.http.HttpResponse;
import com.codingfeel.sm.http.HttpUtils;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.UserModel;
import com.codingfeel.sm.utils.DateUtil;
import com.codingfeel.sm.utils.EncryptUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.MiscUtils;
import com.codingfeel.sm.utils.PushUtil;
import com.codingfeel.sm.utils.SharePrefUtil;
import com.codingfeel.sm.utils.ValidateUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;

/**
 * Created by Heboot on 16/6/24.
 */
public class UserService extends HttpService {

    private static UserService userService;
    private static boolean isLogin = false;
    private static UserModel userModel;

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean loginStatus, UserModel userModel) {
        this.isLogin = loginStatus;
        this.userModel = userModel;
        SharePrefUtil.saveObj(MyApplication.getInstance(), SharePrefKey.PREFERENCES_USER_MODEL, userModel);
    }

    public String getLoginName() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String loginName = "炸";
        loginName += DateUtil.getCurDateStr(DateUtil.FORMAT_MD);
        loginName += chars.charAt((int) (Math.random() * 26));
        loginName += chars.charAt((int) (Math.random() * 26));
        loginName += chars.charAt((int) (Math.random() * 26));
        return loginName;
    }


    public void autoLogin(Context context) {
        String wxCode = SharePrefUtil.getString(context, SharePrefKey.PREFERENCES_WX_CODE, "");
        UserModel sharePreUserModel = getSharePrefUser(context);
        LogUtils.e("===========", "===========autoLogin");
        if (sharePreUserModel != null) {
            LogUtils.e("===========", "===========autoLogin2");
            login(sharePreUserModel.getNickName(), EncryptUtils.md5(sharePreUserModel.getNickName() + MiscUtils.getIMEI(context)), MiscUtils.getIMEI(context), PushUtil.getPushClientId(context));
        } else {
            String loginName = getLoginName();
            register(loginName, EncryptUtils.md5(loginName + MiscUtils.getIMEI(context)), MiscUtils.getIMEI(context), PushUtil.getPushClientId(context));
        }
    }

    public UserModel getLoginUser() {
        return userModel;
    }

    /**
     * 获取本地sharepref中的user
     *
     * @param context
     * @return
     */
    public UserModel getSharePrefUser(Context context) {
        return (UserModel) SharePrefUtil.getObj(context, SharePrefKey.PREFERENCES_USER_MODEL);
    }


    public void register(String loginName, String password, String deviceId, String clientId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_REGISTER, HttpRequest.Method.POST, true);
        httpRequest.addParams(PARAM_LOGINNAME, loginName);
        httpRequest.addParams(PARAM_PASSWORD, password);
        httpRequest.addParams(PARAM_DEVICE_ID, deviceId);
        httpRequest.addParams(PARAM_CLIENT_ID, clientId == null ? "" : clientId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_LOGINNAME, loginName)
                .add(PARAM_PASSWORD, password)
                .add(PARAM_DEVICE_ID, deviceId)
                .add(PARAM_WX_UNIONID, deviceId)
                .add(PARAM_CLIENT_ID, clientId == null ? "" : clientId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                UserModel userModel = JSON.parseObject(result, UserModel.class);
                if (!ValidateUtil.hasError(userModel)) {
                    LogUtils.e("userservice", "login suc save user to local");
                    UserService.getInstance().setIsLogin(true, userModel);
                    SharePrefUtil.saveObj(MyApplication.getInstance(), SharePrefKey.PREFERENCES_USER_MODEL, userModel);
                }
                CommonService.getInstance().home();
                CommonService.getInstance().getMessage();
                EventBus.getDefault().post(new UserEvent.UserLoginEvent(userModel));
            }
        });
    }


    public void login(String loginName, String password, String deviceId, String clientId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_LOGIN, HttpRequest.Method.POST, true);
        httpRequest.addParams(PARAM_LOGINNAME, loginName);
        httpRequest.addParams(PARAM_PASSWORD, password);
        httpRequest.addParams(PARAM_DEVICE_ID, deviceId);
        httpRequest.addParams(PARAM_CLIENT_ID, clientId == null ? "" : clientId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_LOGINNAME, loginName)
                .add(PARAM_PASSWORD, password)
                .add(PARAM_DEVICE_ID, deviceId)
                .add(PARAM_WX_UNIONID, deviceId)
                .add(PARAM_CLIENT_ID, clientId == null ? "" : clientId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                UserModel userModel = JSON.parseObject(result, UserModel.class);
                if (!ValidateUtil.hasError(userModel)) {
                    LogUtils.e("userservice", "login suc save user to local");
                    UserService.getInstance().setIsLogin(true, userModel);
                    SharePrefUtil.saveObj(MyApplication.getInstance(), SharePrefKey.PREFERENCES_USER_MODEL, userModel);
                }
                CommonService.getInstance().home();
                CommonService.getInstance().getMessage();
                EventBus.getDefault().post(new UserEvent.UserLoginEvent(userModel));
            }
        });
    }


    /**
     * 用户登录
     *
     * @param loginName
     * @param password
     * @param deviceId
     */
    public void wxLogin(String wxCode, String deviceId, String clientId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_LOGIN_WX, HttpRequest.Method.POST, true);
        httpRequest.addParams(PARAM_WXCODE, wxCode);
        httpRequest.addParams(PARAM_DEVICE_ID, deviceId);
        httpRequest.addParams(PARAM_CLIENT_ID, clientId == null ? "" : clientId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_WXCODE, wxCode)
                .add(PARAM_DEVICE_ID, deviceId)
                .add(PARAM_WX_UNIONID, deviceId)
                .add(PARAM_CLIENT_ID, clientId == null ? "" : clientId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                UserModel userModel = JSON.parseObject(result, UserModel.class);
                if (!ValidateUtil.hasError(userModel)) {
                    LogUtils.e("userservice", "login suc save user to local");
                    UserService.getInstance().setIsLogin(true, userModel);
                    SharePrefUtil.saveObj(MyApplication.getInstance(), SharePrefKey.PREFERENCES_USER_MODEL, userModel);
                }
                CommonService.getInstance().home();
                CommonService.getInstance().getMessage();
                EventBus.getDefault().post(new UserEvent.UserLoginEvent(userModel));
            }
        });
    }


    /**
     * 获取用户的优惠列表
     *
     * @param pageNo
     * @param pageSize
     */
    public void getMyInfo(int pageNo, int pageSize) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_MYINFO, HttpRequest.Method.GET, false);
        httpRequest.addParams(PARAM_PAGESIZE, String.valueOf(pageSize));
        httpRequest.addParams(PARAM_PAGENO, String.valueOf(pageNo));

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_PAGESIZE, String.valueOf(pageSize))
                .add(PARAM_PAGENO, String.valueOf(pageNo))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                MyInfoBean myInfoBean = JSON.parseObject(result, MyInfoBean.class);
                EventBus.getDefault().post(new UserEvent.UserMyInfoEvent(myInfoBean));
            }
        });
    }


    /**
     * 获取用户的优惠列表
     *
     * @param pageNo
     * @param pageSize
     */
    public void getMyPost(int pageNo, int pageSize) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_MYPOST, HttpRequest.Method.GET, false);
        httpRequest.addParams(PARAM_PAGESIZE, String.valueOf(pageSize));
        httpRequest.addParams(PARAM_PAGENO, String.valueOf(pageNo));

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_PAGESIZE, String.valueOf(pageSize))
                .add(PARAM_PAGENO, String.valueOf(pageNo))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                MyPostBean myPostBean = JSON.parseObject(result, MyPostBean.class);
                EventBus.getDefault().post(new UserEvent.UserMyPostEvent(myPostBean));
            }
        });
    }

    /**
     * 获取用户的优惠列表
     *
     * @param pageNo
     * @param pageSize
     */
    public void getMyFav(int pageNo, int pageSize) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_MYFAV, HttpRequest.Method.GET, false);
        httpRequest.addParams(PARAM_PAGESIZE, String.valueOf(pageSize));
        httpRequest.addParams(PARAM_PAGENO, String.valueOf(pageNo));

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_PAGESIZE, String.valueOf(pageSize))
                .add(PARAM_PAGENO, String.valueOf(pageNo))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                MyFavBean myPostBean = JSON.parseObject(result, MyFavBean.class);
                EventBus.getDefault().post(new UserEvent.UserMyFavEvent(myPostBean));
            }
        });
    }


    public void delInfo(final String infoId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_MYINFO, HttpRequest.Method.DELETE, false);
        httpRequest.addParams(PARAM_INFOID, infoId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_INFOID, infoId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new InfoEvent.InfoDelEvent(infoId, baseModel));
            }
        });
    }


    public void delPost(final String postId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_MYPOST, HttpRequest.Method.DELETE, false);
        httpRequest.addParams(PARAM_POSTID, postId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new PostEvent.PostDelEvent(baseModel, postId));
            }
        });
    }


//    public void delFav(String postId) {
//        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_MYFAV, HttpRequest.Method.DELETE, false);
//        httpRequest.addParams(PARAM_POSTID, postId);
//
//        FormBody formBody = new FormBody.Builder()
//                .add(PARAM_POSTID, postId)
//                .build();
//        httpRequest.setFormBody(formBody);
//
//        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(String result) {
//                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
//                EventBus.getDefault().post(new BaseEvent.BasicEvent(baseModel, BasicEventEnum.POST_FAV_DEL));
//            }
//        });
//    }

    public void userInfo(final String name, final String avatar) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_USER_INFO, HttpRequest.Method.PUT, true);
        httpRequest.addParams(PARAM_NAME, name);
        httpRequest.addParams(PARAM_AVATAR, avatar);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_NAME, name)
                .add(PARAM_AVATAR, avatar)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                if (!ValidateUtil.hasError(baseModel)) {
                    if (!TextUtils.isEmpty(name)) {
                        UserService.getInstance().getLoginUser().setNickName(name);
                    }
                    if (!TextUtils.isEmpty(avatar)) {
                        UserService.getInstance().getLoginUser().setAvatar(avatar);
                    }
                }
                EventBus.getDefault().post(new BaseEvent.BasicEvent(baseModel, BasicEventEnum.USER_INFO));
            }
        });
    }


}
