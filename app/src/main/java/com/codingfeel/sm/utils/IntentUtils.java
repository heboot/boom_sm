package com.codingfeel.sm.utils;

import android.content.Context;
import android.content.Intent;

import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.enums.HtmlFrom;
import com.codingfeel.sm.model.InfoModel;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.service.InfoService;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.ui.common.CropActivity;
import com.codingfeel.sm.ui.common.HtmlActivity;
import com.codingfeel.sm.ui.info.InfoDetailActivity;
import com.codingfeel.sm.ui.info.MyInfoActivity;
import com.codingfeel.sm.ui.info.NewInfoActivity;
import com.codingfeel.sm.ui.common.CommentsActivity;
import com.codingfeel.sm.ui.message.SystemMessageActivity;
import com.codingfeel.sm.ui.my.EditTextActivity;
import com.codingfeel.sm.ui.my.FeedbackActivity;
import com.codingfeel.sm.ui.my.MyFavActivity;
import com.codingfeel.sm.ui.my.UserInfoActivity;
import com.codingfeel.sm.ui.my.SettingActivity;
import com.codingfeel.sm.ui.post.EditPostActivity;
import com.codingfeel.sm.ui.my.MyPostActivity;
import com.codingfeel.sm.ui.post.NewPostActivity;
import com.codingfeel.sm.ui.post.PostDetailActivity;
import com.codingfeel.sm.ui.search.SearchResultActivity;
import com.codingfeel.sm.ui.user.LoginActivity;


/**
 * Created by Heboot on 16/6/28.
 */
public class IntentUtils {


    public static void intent2InfoDetailActivity(Context context, InfoModel infoModel) {
        InfoService.getInstance().read(infoModel.getInfoId());
        Intent intent = new Intent(context, InfoDetailActivity.class);
        intent.putExtra(ContentKey.INFO_MODEL, infoModel);
        context.startActivity(intent);
    }

    public static void intent2PostDetailActivity(Context context, PostModel postModel) {
        PostService.getInstance().read(postModel.getPostId());
        PostModel memoryPostModel = PostService.getInstance().loadPostModelsSync().get(postModel.getPostId());
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(ContentKey.POST_MODEL, memoryPostModel == null ? postModel : memoryPostModel);
        context.startActivity(intent);
    }

    public static void intent2InfoCommentActivity(Context context, String infoId, InfoModel infoModel) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(ContentKey.PAGEJUMP_COMMENT, ContentKey.PAGEJUMP_COMMENT_INFO);
        intent.putExtra(ContentKey.INFO_ID, infoId);
        intent.putExtra(ContentKey.INFO_MODEL, infoModel);
        context.startActivity(intent);
    }

    public static void intent2PostCommentActivity(Context context, String postId, PostModel postModel) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(ContentKey.PAGEJUMP_COMMENT, ContentKey.PAGEJUMP_COMMENT_POST);
        intent.putExtra(ContentKey.POST_ID, postId);
        intent.putExtra(ContentKey.POST_MODEL, postModel);
        context.startActivity(intent);
    }

    public static void intent2NewPostActivity(Context context) {
        Intent intent = new Intent(context, NewPostActivity.class);
        context.startActivity(intent);
    }

    public static void intent2NewInfoActivity(Context context) {
        Intent intent = new Intent(context, NewInfoActivity.class);
        context.startActivity(intent);
    }

    public static void intent2MyInfoActivity(Context context) {
        Intent intent = new Intent(context, MyInfoActivity.class);
        context.startActivity(intent);
    }

    public static void intent2MyPostActivity(Context context) {
        Intent intent = new Intent(context, MyPostActivity.class);
        context.startActivity(intent);
    }

    public static void intent2LoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void intent2CropActivity(Context context, String imagePath, String cropType) {
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(ContentKey.IMAGE_PATH, imagePath);
        intent.putExtra(ContentKey.CROP_TYPE, cropType);
        context.startActivity(intent);
    }

    public static void intent2HmtlActivity(Context context, String htmlUrl, HtmlFrom from) {
        Intent intent = new Intent(context, HtmlActivity.class);
        intent.putExtra(ContentKey.HTML_URL, htmlUrl);
        intent.putExtra(ContentKey.HTML_FROM, from.toString());
        context.startActivity(intent);
    }

    public static void intent2SearchResultActivity(Context context, String searchType) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(ContentKey.SEARCH_TYPE, searchType);
        context.startActivity(intent);
    }

    public static void intent2EditPostActivity(Context context, PostModel postModel) {
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.putExtra(ContentKey.POST_MODEL, postModel);
        context.startActivity(intent);
    }

    public static void intent2SystemMessageActivity(Context context) {
        Intent intent = new Intent(context, SystemMessageActivity.class);
        context.startActivity(intent);
    }


    public static void intent2FeedbackActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    public static void intent2SettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    public static void intent2MyFavActivity(Context context) {
        Intent intent = new Intent(context, MyFavActivity.class);
        context.startActivity(intent);
    }

    public static void intent2MyInformationActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    public static void intent2EditTextActivity(Context context) {
        Intent intent = new Intent(context, EditTextActivity.class);
        context.startActivity(intent);
    }

}
