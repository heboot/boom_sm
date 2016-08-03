/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.codingfeel.sm.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.enums.ShareType;
import com.codingfeel.sm.event.InfoEvent;
import com.codingfeel.sm.event.PostEvent;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(this, ConstantValue.WX_APPID, false);
        //将应用注册到微信
        iwxapi.registerApp(ConstantValue.WX_APPID);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (null != baseResp && baseResp instanceof SendAuth.Resp) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                //微信登录
//                UserService.getInstance().login(resp.code, MiscUtils.getIMEI(this), PushUtil.getPushClientId(this));
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                finish();
            }
        } else if (null != baseResp && baseResp instanceof SendMessageToWX.Resp) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                if (MyApplication.getInstance().getShareType() == ShareType.INFO) {
                    EventBus.getDefault().post(new InfoEvent.InfoShareSucEvent());
                } else if (MyApplication.getInstance().getShareType() == ShareType.POST) {
                    EventBus.getDefault().post(new PostEvent.PostShareSucEvent());
                }
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                //EventBus.getDefault().post(new ShareEvent.ShareSuccessEvent());
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                //EventBus.getDefault().post(new ShareEvent.ShareCancelEvent());
                Toast.makeText(this, "分享取消", Toast.LENGTH_SHORT).show();
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                Toast.makeText(this, "分享被拒绝", Toast.LENGTH_SHORT).show();
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_SENT_FAILED) {
                Toast.makeText(this, "分享发送失败", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
