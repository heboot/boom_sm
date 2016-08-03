package com.codingfeel.sm.utils;

import android.content.Context;
import android.text.TextUtils;

import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.dao.DaoMaster;
import com.codingfeel.sm.dao.DaoSession;
import com.codingfeel.sm.dao.MessageModelDao;
import com.codingfeel.sm.enums.MessageLocalType;
import com.codingfeel.sm.model.MessageModel;

import java.util.List;
import java.util.UUID;

/**
 * Created by Heboot on 16/7/27.
 */
public class DBUtil {

    private final static String DB_NAME = "sanhaolou.db";
    private static volatile DBUtil dbUtil;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private MessageModelDao messageModelDao;

    public static DBUtil getInstance() {
        if (null == dbUtil) {
            synchronized (DBUtil.class) {
                if (null == dbUtil) {
                    dbUtil = new DBUtil();
                    dbUtil.daoSession = dbUtil.getDaoSession(MyApplication.getInstance());
                    dbUtil.messageModelDao = dbUtil.daoSession.getMessageModelDao();
                }
            }
        }
        return dbUtil;
    }


    private DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 消息入库
     */
    public void saveMessage(MessageModel messageModel) {

        switch (messageModel.getType()) {
            case ConstantValue.MESSAGE_TYPE_COMMENT_POST:
            case ConstantValue.MESSAGE_TYPE_COMMENT_POST_RE:
            case ConstantValue.MESSAGE_TYPE_COMMENT_INFO:
            case ConstantValue.MESSAGE_TYPE_COMMENT_INFO_RE:
            case ConstantValue.MESSAGE_TYPE_INFO_VERIFY:
            case ConstantValue.MESSAGE_TYPE_POST_VERIFY:
                messageModel.setLocalType(MessageLocalType.NORMAL.getValue());
                break;
            case ConstantValue.MESSAGE_TYPE_SYSTEM_WEB:
            case ConstantValue.MESSAGE_TYPE_SYSTEM_INFO:
            case ConstantValue.MESSAGE_TYPE_SYSTEM_POST:
                messageModel.setLocalType(MessageLocalType.SYSTEM.getValue());
                break;
        }

        messageModel.setStatus(0);


        if (!TextUtils.isEmpty(messageModel.getUuid())) {
            messageModel.setUuid(UUID.randomUUID().toString());
        }


        messageModelDao.insert(messageModel);
    }

    /**
     * 删除消息
     */
    public void delMessage(String messageId) {
        MessageModel messageModel = messageModelDao.queryBuilder().where(MessageModelDao.Properties.Uuid.eq(messageId)).unique();
        messageModel.setStatus(-1);
        messageModelDao.update(messageModel);
    }

    /**
     * 获取消息页面的消息列表
     */
    public List<MessageModel> getMessageList(int uid) {
        return messageModelDao.queryBuilder().where(MessageModelDao.Properties.Uid.eq(uid), MessageModelDao.Properties.Status.eq(0), MessageModelDao.Properties.LocalType.eq(MessageLocalType.NORMAL.getValue())).list();
    }

    /**
     * 获取所有的系统消息
     */
    public List<MessageModel> getSystemMessageList(int uid) {
        return messageModelDao.queryBuilder().where( MessageModelDao.Properties.Status.eq(0), MessageModelDao.Properties.LocalType.eq(MessageLocalType.SYSTEM.getValue())).list();
    }

    /**
     * 获取最后一条系统消息
     */
    public MessageModel getLastSystemMessage(int uid) {
        List<MessageModel> messageModels = messageModelDao.queryBuilder().where(MessageModelDao.Properties.LocalType.eq(MessageLocalType.SYSTEM.getValue())).orderDesc(MessageModelDao.Properties.CreateTime).list();
        if (messageModels != null && messageModels.size() > 0) {
            return messageModels.get(0);
        }
        return null;
    }


}
