package com.codingfeel.sm.utils;

import android.content.Context;

import com.codingfeel.sm.common.ConstantValue;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Heboot on 16/7/28.
 */
public class WXUtil {

    private static IWXAPI api;

    private static WXUtil wxUtil;

    public static WXUtil getInstance(Context context) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(context, ConstantValue.WX_APPID);
            api.registerApp(ConstantValue.WX_APPID);
        }
        if (wxUtil == null) {
            wxUtil = new WXUtil();
        }
        return wxUtil;
    }


    public static void shareText() {

    }

    public void doLogin() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "codeboom";
        api.sendReq(req);
    }


    public void shareWebpageObject(String url, String title, String desc) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;

//        GetMessageFromWX.Resp resp = new GetMessageFromWX.Resp();
//        resp.transaction = String.valueOf(System.currentTimeMillis());
//        resp.message = msg;
//
//        api.sendResp(resp);


        SendMessageToWX.Req req = new SendMessageToWX.Req();

        req.message = msg;
        req.transaction = String.valueOf(System.currentTimeMillis());

        api.sendReq(req);
    }


//    private static String getTransaction() {
//        final GetMessageFromWX.Req req = new GetMessageFromWX.Req(bundle);
//        return req.transaction;
//    }

}
