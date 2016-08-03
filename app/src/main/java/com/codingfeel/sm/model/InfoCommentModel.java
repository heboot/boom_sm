package com.codingfeel.sm.model;

/**
 * Created by Heboot on 16/7/14.
 */
public class InfoCommentModel extends BaseModel {

    private int commentId;
    private String infoId;
    private int uid;
    private int reUid;
    private String content;
    private long createTime;
    private String avatar;
    private String nickName;
    private String reNickName;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getReUid() {
        return reUid;
    }

    public void setReUid(int reUid) {
        this.reUid = reUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getReNickName() {
        return reNickName;
    }

    public void setReNickName(String reNickName) {
        this.reNickName = reNickName;
    }
}
