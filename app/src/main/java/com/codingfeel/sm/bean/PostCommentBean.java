package com.codingfeel.sm.bean;

import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostCommentModel;

import java.util.List;

/**
 * Created by Heboot on 16/7/14.
 */
public class PostCommentBean extends BaseModel {

    private boolean isLastPage;

    private List<PostCommentModel> comments;

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public List<PostCommentModel> getComments() {
        return comments;
    }

    public void setComments(List<PostCommentModel> comments) {
        this.comments = comments;
    }
}
