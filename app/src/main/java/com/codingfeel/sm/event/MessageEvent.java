package com.codingfeel.sm.event;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.MessageModel;

/**
 * Created by Heboot on 16/7/27.
 */
public class MessageEvent {

    public static class NewMessageEvent {

        private MessageModel messageModel;

        public NewMessageEvent(MessageModel messageModel) {
            this.messageModel = messageModel;
        }

        public MessageModel getMessageModel() {
            return messageModel;
        }
    }

    public static class MessageRefreshEvent {

        public MessageRefreshEvent() {
        }
    }

    public static class MessageDelEvent {


        private BaseModel baseModel;
        private String messageId;

        public MessageDelEvent() {

        }

        public MessageDelEvent(BaseModel baseModel, String messageId) {
            this.baseModel = baseModel;
            this.messageId = messageId;
        }

        public String getMessageId() {
            return messageId;
        }

        public BaseModel getBaseModel() {
            return baseModel;
        }
    }


}
