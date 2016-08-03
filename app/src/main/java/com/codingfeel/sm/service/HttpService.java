package com.codingfeel.sm.service;

/**
 * Created by Heboot on 16/6/26.
 */
public abstract class HttpService {
    String ACTION_VERSION = "/v1";


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
    String ACTION_COMMON_HOME = ACTION_VERSION + "/common/home";
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
}
