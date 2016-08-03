package com.codingfeel.sm.service;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.R;
import com.codingfeel.sm.bean.PostCommentBean;
import com.codingfeel.sm.bean.PostHomeBean;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.PostEvent;
import com.codingfeel.sm.http.HttpRequest;
import com.codingfeel.sm.http.HttpResponse;
import com.codingfeel.sm.http.HttpUtils;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.PostModel;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;

/**
 * Created by Heboot on 16/7/10.
 */
public class PostService extends HttpService {

    private static PostService postService;

    private HashMap<String, PostModel> postModelHashMap = new HashMap<>();

    public static PostService getInstance() {
        if (postService == null) {
            postService = new PostService();
        }
        return postService;
    }

    public Map<String, PostModel> loadPostModelsSync() {
        return new HashMap<String, PostModel>(this.postModelHashMap);
    }

    /**
     * 保存到内存
     *
     * @param postModel
     */
    public void savePostModel(PostModel postModel) {
        this.postModelHashMap.put(postModel.getPostId(), postModel);
    }

    /**
     * 更新优惠信息评论数
     *
     * @param postId
     * @param count
     */
    public void updatePostModelCommentCount(String postId, int count) {
        this.postModelHashMap.get(postId).setCommentCount(count);
    }

    /**
     * 更新优惠信息赞数
     *
     * @param postId
     * @param count
     */
    public void updatePostModelEvaCount(String postId, int count, boolean isEva) {
        this.postModelHashMap.get(postId).setEva(isEva);
        this.postModelHashMap.get(postId).setEvaCount(count);

    }

    /**
     * 更新收藏数
     *
     * @param postId
     * @param count
     */
    public void updatePostModelFavCount(String postId, int count, boolean isFav) {
        this.postModelHashMap.get(postId).setFav(isFav);
        this.postModelHashMap.get(postId).setFavCount(count);

    }


    public String getPostType(Context context, int type) {
        switch (type) {
            case ConstantValue.POST_TYPE_CEPING:
                return context.getResources().getString(R.string.post_type_ceping);
            case ConstantValue.POST_TYPE_SHAIWU:
                return context.getResources().getString(R.string.post_type_shaiwu);
            case ConstantValue.POST_TYPE_ZHUANGBI:
                return "装哔";
        }
        return null;
    }


    public void post(String title, String content, String imgUrl, int postType) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_POST, HttpRequest.Method.POST, false);
        httpRequest.addParams(PARAM_TITLE, title);
        httpRequest.addParams(PARAM_CONTENT, content);
        httpRequest.addParams(PARAM_IMG_URL, imgUrl);
        httpRequest.addParams(PARAM_TYPE, String.valueOf(postType));

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_TITLE, title)
                .add(PARAM_CONTENT, content)
                .add(PARAM_IMG_URL, imgUrl)
                .add(PARAM_TYPE, String.valueOf(postType))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new BaseEvent.BasicEvent(baseModel, BasicEventEnum.POST_POST));
            }
        });
    }

    public void detail(String postId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_DETAIL, HttpRequest.Method.GET, true);
        httpRequest.addParams(PARAM_POSTID, postId);
        httpRequest.addParams(PARAM_APP, "1");

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .add(PARAM_APP, "1")
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(String result) {
                PostModel postModel = JSON.parseObject(result, PostModel.class);
                EventBus.getDefault().post(new PostEvent.PostDetailEvent(postModel));
            }
        });
    }


    public void home(int pageSize, int pageNo, final String keywords) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_HOME, HttpRequest.Method.GET, true);
        httpRequest.addParams(PARAM_PAGESIZE, String.valueOf(pageSize));
        httpRequest.addParams(PARAM_PAGENO, String.valueOf(pageNo));
        httpRequest.addParams(PARAM_KEYWORDS, keywords);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_KEYWORDS, keywords)
                .add(PARAM_PAGESIZE, String.valueOf(pageSize))
                .add(PARAM_PAGENO, String.valueOf(pageNo))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                PostHomeBean postHomeBean = JSON.parseObject(result, PostHomeBean.class);
                if (!TextUtils.isEmpty(keywords)) {
                    EventBus.getDefault().post(new PostEvent.PostSearchEvent(postHomeBean));
                } else {
                    EventBus.getDefault().post(new PostEvent.PostHomeEvent(postHomeBean));
                }

            }
        });
    }

    public void read(String postId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_READ, HttpRequest.Method.POST, false);
        httpRequest.addParams(PARAM_POSTID, postId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
            }
        });
    }


    public void comment(String postId, String content, Integer reUid) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_COMMENT, HttpRequest.Method.POST, false);
        httpRequest.addParams(PARAM_POSTID, postId);
        httpRequest.addParams(PARAM_CONTENT, content);
        httpRequest.addParams(PARAM_REUID, reUid == null ? "" : String.valueOf(reUid));

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .add(PARAM_CONTENT, content)
                .add(PARAM_REUID, reUid == null ? "" : String.valueOf(reUid))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new BaseEvent.BasicEvent(baseModel, BasicEventEnum.POST_COMMENT));
            }
        });
    }


    public void getComments(String postId, int pageSize, int pageNo) {

        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_COMMENT, HttpRequest.Method.GET, true);
        httpRequest.addParams(PARAM_PAGESIZE, String.valueOf(pageSize));
        httpRequest.addParams(PARAM_PAGENO, String.valueOf(pageNo));
        httpRequest.addParams(PARAM_POSTID, postId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .add(PARAM_PAGESIZE, String.valueOf(pageSize))
                .add(PARAM_PAGENO, String.valueOf(pageNo))
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                PostCommentBean postCommentBean = JSON.parseObject(result, PostCommentBean.class);
                EventBus.getDefault().post(new PostEvent.PostCommentsEvent(postCommentBean));
            }
        });

    }


    public void fav(final String postId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_FAV, HttpRequest.Method.POST, false);
        httpRequest.addParams(PARAM_POSTID, postId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new PostEvent.PostFavEvent(baseModel, postId));
            }
        });
    }


    public void eva(String postId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_EVA, HttpRequest.Method.POST, false);
        httpRequest.addParams(PARAM_POSTID, postId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new BaseEvent.BasicEvent(baseModel, BasicEventEnum.POST_EVA));
            }
        });
    }

    public void share(String postId) {
        HttpRequest httpRequest = new HttpRequest(BuildConfig.HTTP_SERVER + ACTION_POST_SHARE, HttpRequest.Method.POST, false);
        httpRequest.addParams(PARAM_POSTID, postId);

        FormBody formBody = new FormBody.Builder()
                .add(PARAM_POSTID, postId)
                .build();
        httpRequest.setFormBody(formBody);

        HttpUtils.getInstance().execute(httpRequest, new HttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(String result) {
                BaseModel baseModel = JSON.parseObject(result, BaseModel.class);
                EventBus.getDefault().post(new BaseEvent.BasicEvent(baseModel, BasicEventEnum.POST_SHARE));
            }
        });
    }


    /**
     * 获取帖子第一行文本内容 前15字
     */
    public String getPostShareContent(String content) {
        Elements pElements = Jsoup.parse(content).getElementsByTag("p");
        String shareContent = "";
        if (pElements.size() > 0) {
            shareContent = pElements.get(0).getElementsByTag("p").text();
            if (shareContent.length() > 15) {
                return shareContent.substring(0, 15) + "...";
            } else {
                return shareContent;
            }
        }
        return "";
    }


}
