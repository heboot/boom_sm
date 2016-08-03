package com.codingfeel.sm.model;

import org.greenrobot.greendao.annotation.*;

import java.util.Date;

/**
 * Created by Heboot on 16/7/27.
 */
@Entity
public class MessageModel {

    @Id
    private String uuid;
    private Integer uid;
    private String clientId;
    private Integer type;
    private Integer status;
    private String infoId;
    private Integer infoCommentId;
    private String postId;
    private Integer postCommentId;
    private String pushTitle;
    private String pushContent;
    private String messageTitle;
    private String messageContent;
    private String messageExt1;
    private String messageExt2;
    private Date createTime;
    private int localType;
    @Generated(hash = 1699352037)
    public MessageModel() {
    }
    @Generated(hash = 646780248)
    public MessageModel(String uuid, Integer uid, String clientId, Integer type, Integer status, String infoId, Integer infoCommentId, String postId, Integer postCommentId, String pushTitle, String pushContent, String messageTitle, String messageContent, String messageExt1, String messageExt2, Date createTime, int localType, String avatar) {
        this.uuid = uuid;
        this.uid = uid;
        this.clientId = clientId;
        this.type = type;
        this.status = status;
        this.infoId = infoId;
        this.infoCommentId = infoCommentId;
        this.postId = postId;
        this.postCommentId = postCommentId;
        this.pushTitle = pushTitle;
        this.pushContent = pushContent;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
        this.messageExt1 = messageExt1;
        this.messageExt2 = messageExt2;
        this.createTime = createTime;
        this.localType = localType;
        this.avatar = avatar;
    }

    /**
     * 头像
     */
    private String avatar;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public Integer getInfoCommentId() {
        return infoCommentId;
    }

    public void setInfoCommentId(Integer infoCommentId) {
        this.infoCommentId = infoCommentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Integer getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(Integer postCommentId) {
        this.postCommentId = postCommentId;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageExt1() {
        return messageExt1;
    }

    public void setMessageExt1(String messageExt1) {
        this.messageExt1 = messageExt1;
    }

    public String getMessageExt2() {
        return messageExt2;
    }

    public void setMessageExt2(String messageExt2) {
        this.messageExt2 = messageExt2;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLocalType() {
        return localType;
    }

    public void setLocalType(int localType) {
        this.localType = localType;
    }


}
