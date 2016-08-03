package com.codingfeel.sm.bean;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.MessageModel;

import java.util.List;

/**
 * Created by Heboot on 16/7/27.
 */
public class MessageBean extends BaseModel {

    private List<MessageModel> userMessages;
    private List<MessageModel> systemMessages;

    public List<MessageModel> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(List<MessageModel> userMessages) {
        this.userMessages = userMessages;
    }

    public List<MessageModel> getSystemMessages() {
        return systemMessages;
    }

    public void setSystemMessages(List<MessageModel> systemMessages) {
        this.systemMessages = systemMessages;
    }
}
