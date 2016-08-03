package com.codingfeel.sm.event;

import com.codingfeel.sm.bean.PostCommentBean;
import com.codingfeel.sm.bean.PostHomeBean;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;

/**
 * Created by Heboot on 16/7/10.
 */
public class PostEvent {

    public static class PostHomeEvent {
        private PostHomeBean postHomeBean;

        public PostHomeEvent(PostHomeBean postHomeBean) {
            this.postHomeBean = postHomeBean;
        }

        public PostHomeBean getPostHomeBean() {
            return postHomeBean;
        }

    }

    public static class PostSearchEvent {
        private PostHomeBean postHomeBean;

        public PostSearchEvent(PostHomeBean postHomeBean) {
            this.postHomeBean = postHomeBean;
        }

        public PostHomeBean getPostHomeBean() {
            return postHomeBean;
        }

    }

    public static class PostCommentsEvent {

        private PostCommentBean postCommentBean;

        public PostCommentsEvent(PostCommentBean postCommentBean) {
            this.postCommentBean = postCommentBean;
        }

        public PostCommentBean getPostCommentBean() {
            return postCommentBean;
        }

        public void setPostCommentBean(PostCommentBean postCommentBean) {
            this.postCommentBean = postCommentBean;
        }
    }


    public static class PostCommentUpdateEvent {
        private String postId;

        public String getPostId() {
            return postId;
        }

        public PostCommentUpdateEvent(String postId) {
            this.postId = postId;
        }
    }


    public static class PostShareSucEvent {
        public PostShareSucEvent() {
        }
    }

    public static class PostDelEvent {
        private BaseModel baseModel;
        private String postId;

        public BaseModel getBaseModel() {
            return baseModel;
        }

        public PostDelEvent(BaseModel baseModel, String postId) {
            this.baseModel = baseModel;
            this.postId = postId;
        }

        public String getPostId() {
            return postId;
        }

    }

    public static class PostFavEvent {
        private BaseModel baseModel;
        private String postId;

        public BaseModel getBaseModel() {
            return baseModel;
        }

        public PostFavEvent(BaseModel baseModel, String postId) {
            this.baseModel = baseModel;
            this.postId = postId;
        }

        public String getPostId() {
            return postId;
        }
    }

    public static class PostDetailEvent {
        public PostDetailEvent(PostModel postModel) {
            this.postModel = postModel;
        }

        private PostModel postModel;

        public PostModel getPostModel() {
            return postModel;
        }

    }


}
