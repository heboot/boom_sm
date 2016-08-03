package com.codingfeel.sm.event;


import com.codingfeel.sm.bean.MyFavBean;
import com.codingfeel.sm.bean.MyInfoBean;
import com.codingfeel.sm.bean.MyPostBean;
import com.codingfeel.sm.model.UserModel;

/**
 * Created by Heboot on 16/6/27.
 */
public class UserEvent {

    public static class UserLoginEvent {

        private UserModel userModel;

        public UserLoginEvent(UserModel userModel) {
            this.userModel = userModel;
        }

        public UserModel getUserModel() {
            return userModel;
        }

        public void setUserModel(UserModel userModel) {
            this.userModel = userModel;
        }
    }


    public static class UserMyInfoEvent {

        private MyInfoBean myInfoBean;

        public UserMyInfoEvent(MyInfoBean bean) {
            this.myInfoBean = bean;
        }

        public MyInfoBean getMyInfoBean() {
            return myInfoBean;
        }
    }

    public static class UserMyPostEvent {

        private MyPostBean myPostBean;

        public UserMyPostEvent(MyPostBean bean) {
            this.myPostBean = bean;
        }

        public MyPostBean getMyPostBean() {
            return myPostBean;
        }
    }

    public static class UserMyFavEvent {

        private MyFavBean myFavBean;

        public UserMyFavEvent(MyFavBean bean) {
            this.myFavBean = bean;
        }

        public MyFavBean getMyFavBean() {
            return myFavBean;
        }

        public void setMyFavBean(MyFavBean myFavBean) {
            this.myFavBean = myFavBean;
        }
    }

}
