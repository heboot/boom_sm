package com.codingfeel.sm.bean;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;

import java.util.List;

/**
 * Created by Heboot on 16/7/26.
 */
public class MyPostBean extends BaseModel {

    private boolean lastPage;

    private List<PostModel> posts;

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostModel> posts) {
        this.posts = posts;
    }
}
