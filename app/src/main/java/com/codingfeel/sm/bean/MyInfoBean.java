package com.codingfeel.sm.bean;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.InfoModel;

import java.util.List;

/**
 * Created by Heboot on 16/7/19.
 */
public class MyInfoBean extends BaseModel{

    private boolean lastPage;

    private List<InfoModel> info;

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<InfoModel> getInfo() {
        return info;
    }

    public void setInfo(List<InfoModel> info) {
        this.info = info;
    }
}
