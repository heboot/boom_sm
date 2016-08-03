package com.codingfeel.sm.event;


import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.model.BaseModel;

/**
 * Created by Heboot on 16/6/27.
 */
public class BaseEvent {

    public static class BasicEvent {

        private BaseModel baseModel;
        private BasicEventEnum basicEventEnum;

        public BasicEvent(BaseModel baseModel, BasicEventEnum basicEventEnum) {
            this.baseModel = baseModel;
            this.basicEventEnum = basicEventEnum;
        }

        public BasicEventEnum getBasicEventEnum() {
            return basicEventEnum;
        }

        public void setBasicEventEnum(BasicEventEnum basicEventEnum) {
            this.basicEventEnum = basicEventEnum;
        }

        public BaseModel getBaseModel() {
            return baseModel;
        }

        public void setBaseModel(BaseModel baseModel) {
            this.baseModel = baseModel;
        }
    }
}
