package com.codingfeel.sm.api;

import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.bean.CommonGuestBean;
import com.codingfeel.sm.http.HttpRequest;
import com.codingfeel.sm.http.HttpUtils;
import com.codingfeel.sm.model.PostTagModel;
import com.codingfeel.sm.model.UserModel;
import com.codingfeel.sm.service.HttpService;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.utils.EncryptUtils;
import com.codingfeel.sm.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Heboot on 2016/12/27.
 */

public class ApiClient {

    static final String ACTION_VERSION = "/v1";


    //用户相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓start
    //登录
    String ACTION_USER_LOGIN_WX = ACTION_VERSION + "/user/weixin-login";
    //反馈
    String ACTION_USER_FEEDBACK = ACTION_VERSION + "/user/feedback";
    //我的优惠列表
    String ACTION_USER_MYINFO = ACTION_VERSION + "/user/my-info";
    String ACTION_USER_MYFAV = ACTION_VERSION + "/user/my-fav";
    String ACTION_USER_INFO = ACTION_VERSION + "/user/user-info";
    String ACTION_USER_LOGIN = ACTION_VERSION + "/user/login";
    String ACTION_USER_REGISTER = ACTION_VERSION + "/user/register";
    //用户相关↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑end


    //优惠信息相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓start
    //获取文章首页列表数据
    String ACTION_INFO_HOME = ACTION_VERSION + "/info/home";
    String ACTION_INFO_EVA = ACTION_VERSION + "/info/eva";
    String ACTION_INFO_COMMENT = ACTION_VERSION + "/info/comment";
    String ACTION_INFO_INFO = ACTION_VERSION + "/info/info";
    String ACTION_INFO_READ = ACTION_VERSION + "/info/read";
    String ACTION_INFO_SHARE = ACTION_VERSION + "/info/share";
    String ACTION_INFO_DETAIL = ACTION_VERSION + "/info/detail";
    //优惠信息相关↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑end

    //帖子相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓start
    //获取帖子首页列表数据
    String ACTION_POST_HOME = ACTION_VERSION + "/post/home";
    String ACTION_POST_COMMENT = ACTION_VERSION + "/post/comment";
    String ACTION_POST_POST = ACTION_VERSION + "/post/post";
    String ACTION_POST_READ = ACTION_VERSION + "/post/read";
    String ACTION_USER_MYPOST = ACTION_VERSION + "/user/my-post";
    String ACTION_POST_FAV = ACTION_VERSION + "/post/fav";
    String ACTION_POST_EVA = ACTION_VERSION + "/post/eva";
    String ACTION_POST_SHARE = ACTION_VERSION + "/post/share";
    String ACTION_POST_DETAIL = ACTION_VERSION + "/post/detail";
    //帖子相关↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑end


    //系统模块相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓start
    //获取app基本信息
    static final String ACTION_COMMON_HOME = ACTION_VERSION + "/common/home";
    String ACTION_COMMON_HOME_GUEST = ACTION_VERSION + "/common/home-guest";
    String ACTION_COMMON_MESSAGE = ACTION_VERSION + "/common/message";
    //系统模块相关↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑end

    String PARAM_NAME = "name";
    String PARAM_WX_UNIONID = "wxUnionid";
    String PARAM_PASSWORD = "password";
    String PARAM_DEVICE_ID = "deviceId";
    String PARAM_KEYWORDS = "keywords";
    String PARAM_PAGESIZE = "pageSize";
    String PARAM_PAGENO = "pageNo";
    String PARAM_POSTID = "postId";
    String PARAM_INFOID = "infoId";
    String PARAM_CONTENT = "content";
    String PARAM_REUID = "reUid";
    String PARAM_TITLE = "title";
    String PARAM_GOODS_URL = "goodsUrl";
    String PARAM_IMG_URL = "imgUrl";
    String PARAM_TYPE = "type";
    String PARAM_APP = "app";
    String PARAM_CLIENT_ID = "clientId";
    String PARAM_MESSAGE_ID = "messageId";
    String PARAM_CREATE_TIME = "createTime";
    String PARAM_AVATAR = "avatar";
    String PARAM_WXCODE = "code";
    String PARAM_LOGINNAME = "loginName";
    String PARAM_MESSAGE_CREATE_TIME = "messageCreateTime";
    String PARAM_MESSAGE_SYSTEM_CREATE_TIME = "systemMessageCreateTime";
    String PARAM_IS_HOT = "isHot";

    private static OkHttpClient client = new OkHttpClient();

    private static String token;

    private static String secret;

    private static String TAG = ApiClient.class.getName();

    public static CommonServiceInterface commonServiceInterface;

    /**
     * 用户ID
     */
    private static Integer uid = 0;

    public static CommonServiceInterface getCommonServiceInterface(ApiRequest request) {
        doSignature(request);
        if (commonServiceInterface == null) {
            Retrofit retrofit = new Retrofit.Builder().
                    baseUrl(BuildConfig.HTTP_SERVER)
                    .client(client)
                    .build();
            return retrofit.create(CommonServiceInterface.class);
        }
        return commonServiceInterface;
    }

    public interface CommonServiceInterface {

        @GET(ACTION_COMMON_HOME)
        Observable<CommonGuestBean> homeGuest();

    }

    /**
     * 初始化头部信息
     */
    private static void doSignature(ApiRequest httpRequest) {
        String time = String.valueOf(new Date().getTime());
        String canonicalizedHeaders = "";
//        headers = new Headers.Builder()
//                .add("x-codingfeel-terminal", "android")
//                .add("x-codingfeel-version", MyApplication.getInstance().getVersion())
//                .add("Charset", "utf-8")
//                .add("x-codingfeel-time", time)

//                .build();
//        httpRequest.addHeader("x-codingfeel-terminal", "android");
        UserModel user = UserService.getInstance().getSharePrefUser(MyApplication.getInstance());

        if (httpRequest.isGuide()) {
            return;
        }


        if (user != null) {
            uid = user.getUid();
            token = user.getToken();
            secret = user.getSecret();
            canonicalizedHeaders =
                    "x-codingfeel-uid:" + uid + "\n" +
                            "x-codingfeel-time:" + time + "\n" +
                            "x-codingfeel-terminal:" + "android\n" +
                            "x-codingfeel-version:" + MyApplication.getInstance().getVersion();
        } else {
            return;
        }


        //请求参数格式化
        String canonicalizedParmas = getCanonicalizedParams(httpRequest);


        //拼接成要加密的内容
        String content =
                httpRequest.matchMethod(httpRequest.getMethod()) + "\n"
                        + getSignNetworkUri(httpRequest) + "\n"
                        + token + "\n"
                        + canonicalizedHeaders + "\n"
                        + canonicalizedParmas + "\n";

        LogUtils.e(TAG + "jiami", content);


        String signature = "";
        //调用签名工具类进行签名
        try {
            String result = new String(android.util.Base64.encode(EncryptUtils.hmacSHA1Encrypt(content, secret), android.util.Base64.DEFAULT | android.util.Base64.NO_WRAP));
            signature = URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


//        headers = new Headers.Builder()
//                .add("x-codingfeel-terminal", "android")
//                .add("x-codingfeel-version", MyApplication.getInstance().getVersion())
//                .add("Charset", "utf-8")
//                .add("x-codingfeel-uid", String.valueOf(uid))
//                .add("x-codingfeel-time", time)
//                .add("Authorization", "Basic " + token + ":" + signature)
//                .build();


//        httpRequest.addHeader("x-codingfeel-terminal", "android");
//        httpRequest.addHeader("x-codingfeel-version", MyApplication.getInstance().getVersion());
//        httpRequest.addHeader("Charset", "utf-8");
//        httpRequest.addHeader("x-codingfeel-uid", String.valueOf(uid));
//        httpRequest.addHeader("x-codingfeel-time", time);
//        httpRequest.addHeader("Authorization", "Basic " + token + ":" + signature);

    }

    private static String getSignNetworkUri(ApiRequest httpRequest) {
        String signUri = httpRequest.getHttpUrl().replaceFirst(BuildConfig.HTTP_SERVER, "");
        if (signUri.contains("?")) {
            int index = signUri.indexOf("?");
            return signUri.substring(0, index);
        } else {
            return signUri;
        }
    }


    private static String getCanonicalizedParams(ApiRequest httpRequest) {


        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : httpRequest.getParams().entrySet()) {
            try {
                paramList.add(new StringBuffer(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8")).toString().replaceAll("\\+", "%20"));

//            paramList.add(new StringBuffer(entry.getKey()).append("=")
//                    .append(entry.getValue()).toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        Collections.sort(paramList);
        StringBuilder paramBuilder = new StringBuilder();
        boolean first = true;
        for (String item : paramList) {
            if (first) {
                first = false;
            } else {
                paramBuilder.append("&");
            }
            paramBuilder.append(item);
        }

        return paramBuilder.toString();
    }


}
