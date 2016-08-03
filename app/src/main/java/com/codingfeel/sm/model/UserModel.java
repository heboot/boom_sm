package com.codingfeel.sm.model;

import java.io.Serializable;

/**
 * Created by Heboot on 16/7/10.
 */
public class UserModel extends BaseModel implements Serializable {

    private Integer uid;
    private Long createTime;
    private String nickName;
    private String secret;
    private String deviceId;
    private int isRegister;
    private String token;
    private String avatar;
    private String wxUnionid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(int isRegister) {
        this.isRegister = isRegister;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWxUnionid() {
        return wxUnionid;
    }

    public void setWxUnionid(String wxUnionid) {
        this.wxUnionid = wxUnionid;
    }
}
