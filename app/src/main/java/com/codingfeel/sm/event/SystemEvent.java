package com.codingfeel.sm.event;

import com.codingfeel.sm.bean.CommonGuestBean;

/**
 * Created by Heboot on 16/8/1.
 */
public class SystemEvent {

    public static class VerisonUpdateEvent {
        private CommonGuestBean commonGuestBean;

        public VerisonUpdateEvent(CommonGuestBean commonGuestBean) {
            this.commonGuestBean = commonGuestBean;
        }

        public CommonGuestBean getCommonGuestBean() {
            return commonGuestBean;
        }

        public void setCommonGuestBean(CommonGuestBean commonGuestBean) {
            this.commonGuestBean = commonGuestBean;
        }
    }


}
