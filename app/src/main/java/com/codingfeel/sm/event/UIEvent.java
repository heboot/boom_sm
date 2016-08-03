package com.codingfeel.sm.event;

/**
 * Created by Heboot on 16/7/31.
 */
public class UIEvent {


    public static class MainBottomEvent {

        private boolean show;

        public MainBottomEvent(boolean show) {
            this.show = show;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }
    }


}
