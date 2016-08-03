package com.codingfeel.sm.bean;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;

import java.util.List;

/**
 * Created by Heboot on 16/7/26.
 */
public class MyFavBean extends BaseModel {

    private boolean lastPage;

    private List<PostModel> fav;

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<PostModel> getFav() {
        return fav;
    }

    public void setFav(List<PostModel> fav) {
        this.fav = fav;
    }
}
