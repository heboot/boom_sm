package com.codingfeel.sm.bean;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.InfoCommentModel;

import java.util.List;

/**
 * Created by Heboot on 16/7/14.
 */
public class InfoCommentBean extends BaseModel {

    private boolean lastPage;

    private List<InfoCommentModel> comments;

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<InfoCommentModel> getComments() {
        return comments;
    }

    public void setComments(List<InfoCommentModel> comments) {
        this.comments = comments;
    }
}
