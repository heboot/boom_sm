package com.codingfeel.sm.event;

import com.codingfeel.sm.bean.InfoCommentBean;
import com.codingfeel.sm.bean.InfoHomeBean;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.InfoModel;

/**
 * Created by Heboot on 16/7/10.
 */
public class InfoEvent {

    public static class InfoHomeEvent {
        private InfoHomeBean infoHomeBean;

        public InfoHomeEvent(InfoHomeBean infoHomeBean) {
            this.infoHomeBean = infoHomeBean;
        }

        public InfoHomeBean getInfoHomeBean() {
            return infoHomeBean;
        }

        public void setInfoHomeBean(InfoHomeBean infoHomeBean) {
            this.infoHomeBean = infoHomeBean;
        }
    }

    public static class InfoSearchEvent {
        private InfoHomeBean infoHomeBean;

        public InfoSearchEvent(InfoHomeBean infoHomeBean) {
            this.infoHomeBean = infoHomeBean;
        }

        public InfoHomeBean getInfoHomeBean() {
            return infoHomeBean;
        }

    }

    public static class InfoShareSucEvent {
        public InfoShareSucEvent() {
        }
    }

    public static class InfoCommentsEvent {

        private InfoCommentBean infoCommentBean;

        public InfoCommentsEvent(InfoCommentBean infoCommentBean) {
            this.infoCommentBean = infoCommentBean;
        }

        public InfoCommentBean getInfoCommentBean() {
            return infoCommentBean;
        }

        public void setInfoCommentBean(InfoCommentBean infoCommentBean) {
            this.infoCommentBean = infoCommentBean;
        }
    }

    public static class InfoEvaUpdateEvent {
        private String infoId;

        public String getInfoId() {
            return infoId;
        }

        public InfoEvaUpdateEvent(String infoId) {
            this.infoId = infoId;
        }
    }

    public static class InfoCommentUpdateEvent {
        private String infoId;

        public String getInfoId() {
            return infoId;
        }

        public InfoCommentUpdateEvent(String infoId) {
            this.infoId = infoId;
        }
    }

    public static class InfoDelEvent {
        private String infoId;
        private BaseModel baseModel;

        public BaseModel getBaseModel() {
            return baseModel;
        }

        public String getInfoId() {
            return infoId;
        }

        public InfoDelEvent(String infoId, BaseModel baseModel) {
            this.infoId = infoId;
            this.baseModel = baseModel;
        }
    }

    public static class InfoDetailEvent {
        private InfoModel infoModel;

        public InfoModel getInfoModel() {
            return infoModel;
        }

        public InfoDetailEvent(InfoModel infoModel) {
            this.infoModel = infoModel;
        }
    }
}
