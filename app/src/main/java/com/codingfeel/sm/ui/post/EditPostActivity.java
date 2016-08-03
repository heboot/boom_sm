package com.codingfeel.sm.ui.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.ImageEvent;
import com.codingfeel.sm.event.QiNiuEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.BitmapModel;
import com.codingfeel.sm.model.EditDataImgModel;
import com.codingfeel.sm.model.EditDataModel;
import com.codingfeel.sm.model.PostModel;
import com.codingfeel.sm.model.PostTagModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.LogUtils;
import com.codingfeel.sm.utils.MultiImageUtil;
import com.codingfeel.sm.utils.QiNiuUtil;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.richeditor.RichEditTextEditor;
import com.github.johnpersano.supertoasts.SuperToast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Heboot on 16/7/18.
 */
public class EditPostActivity extends ToolBarActivity implements View.OnClickListener {

    //    @BindView(R.id.spinner_newpost_tags)
//    AppCompatSpinner spinnerNewpostTags;
//    @BindView(R.id.iv_newpost_img)
//    ImageView ivNewpostImg;
    @BindView(R.id.et_newpost_content)
    RichEditTextEditor etNewpostContent;
    @BindView(R.id.llyt_newpost_bottom)
    LinearLayout llytNewpostBottom;
    @BindView(R.id.iv_newpost_camera)
    TextView ivNewpostCamera;
    @BindView(R.id.iv_newpost_emoji)
    ImageView ivNewpostEmoji;
    //    @BindView(R.id.et_newpost_title)
//    EditText etNewpostTitle;
//    @BindView(R.id.llyt_newpost_top)
//    LinearLayout llytNewpostTop;
    @BindView(R.id.rlyt_newpost_container)
    RelativeLayout rlytNewpostContainer;


    /**
     * 从服务器获得到的帖子 从我的帖子页面传入过来
     */
    private PostModel postModel;

    private String title;
    private int type;
    private String imgUrl;
    private String content;

    private ArrayList<String> mSelectPath;

    private MultiImageSelector multiImageSelector;

    /**
     * 本地路径和七牛路径对应的集合
     */
    private List<EditDataImgModel> editDataImgModels = new ArrayList<>();


    //是否选择封面
    private boolean isCover = false;

    //封面七牛路径
    private String coverQiNiuPath = "";

    private MyTarget myTarget = new MyTarget();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_new_post), true, null);
        showToolBarSend(this);
        initMultiImageSelector();
        initListener();
        initData();
    }

    private void initData() {
        postModel = (PostModel) getIntent().getExtras().get(ContentKey.POST_MODEL);

        //标题
        etNewpostContent.getTopTitle().setText(postModel.getTitle());
        //类型
        List<PostTagModel> tags = MyApplication.getInstance().getPostTagModelList();
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getType() == postModel.getType()) {
                etNewpostContent.getTopSpinner().setSelection(i);
            }
        }

        etNewpostContent.getTopImg().setVisibility(View.VISIBLE);
        etNewpostContent.getTvImg().setVisibility(View.GONE);

        //封面
        ImageUtils.displayImage(this, postModel.getPostImgUrl(), etNewpostContent.getTopImg());
        //内容
        final Elements elements = Jsoup.parse(postModel.getContent()).getAllElements();

        final Elements imgElements = Jsoup.parse(postModel.getContent()).getElementsByTag("img");

        final Elements pElements = Jsoup.parse(postModel.getContent()).getElementsByTag("p");

//        final CountDownLatch latch = new CountDownLatch(imgElements.size());

        final List<BitmapModel> bitmapModels = new ArrayList<>();

        int index = 0;
        for (final Element e : elements) {

            if (e.tagName().equals("p")) {
                index = index + 1;
                etNewpostContent.addEditTextAtIndex(index, e.getElementsByTag("p").text(), elements.size());
            }
            if (e.tagName().equals("img")) {
                index = index + 1;
                final int a = index;
                String imgPath = e.getElementsByTag("img").attr("src");
                Picasso.with(this).load(imgPath).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmapModels.add(new BitmapModel(a, bitmap));
                        etNewpostContent.addImageViewAtIndex(a, bitmap, "", elements.size());
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }
        }
        etNewpostContent.addEditTextAtIndex(etNewpostContent.getAllLayout().getChildCount(), "");


    }

    private class MyTransformation implements Transformation {

        private int bitmap;

        public MyTransformation(int bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = bitmap;
//            LogUtils.e("source.getHeight()="+source.getHeight()+"+source.getWidth()="+source.getWidth()+",targetWidth="+targetWidth);

            if (source.getWidth() == 0) {
                return source;
            }

            //如果图片小于设置的宽度，则返回原图
            if (source.getWidth() < targetWidth) {
                return source;
            } else {
//                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
//                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
//                int targetHeight = (int) (targetWidth / aspectRatio);
//                if (targetHeight != 0 && targetWidth != 0) {
//                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
//                    if (result != source) {
//                        // Same bitmap is returned if sizes are the same
//                        source.recycle();
//                    }
//                    return result;
//                } else {
//                    return source;
//                }
                return null;
//                Bitmap result = BitmapUtil.comp(source);
//                source.recycle();
            }
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    }

    private class MyTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            LogUtils.e("onBitmapLoaded Picasso", from.toString() + "=======" + bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            LogUtils.e("onBitmapFailed Picasso", "=======");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            LogUtils.e("onPrepareLoad Picasso", "=======");
        }
    }


    private void initListener() {
        ivNewpostCamera.setOnClickListener(this);
//        ivNewpostImg.setOnClickListener(this);
        etNewpostContent.initTopSpinner(this, MyApplication.getInstance().getPostTagModelList());
        etNewpostContent.initImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCover = true;
                multiImageSelector.start(EditPostActivity.this, ConstantValue.REQUEST_IMAGE);
            }
        });
        etNewpostContent.initCoverListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCover = true;
                multiImageSelector.start(EditPostActivity.this, ConstantValue.REQUEST_IMAGE);
            }
        });
    }

    /**
     * 初始化内容部分
     */
    private void initContentLayout() {

        String title = etNewpostContent.getTopTitle().getText().toString();

        if (TextUtils.isEmpty(title)) {
            showToast("记得写标题哦", SuperToast.Duration.VERY_SHORT);
            return;
        }

        if (TextUtils.isEmpty(coverQiNiuPath)) {
            showToast("记得选封面哦", SuperToast.Duration.VERY_SHORT);
            return;
        }

        int typePosition = etNewpostContent.getTopSpinner().getSelectedItemPosition();

        int type = MyApplication.getInstance().getPostTagModelList().get(typePosition).getType();


        List<EditDataModel> editDataModels = etNewpostContent.buildEditData();

        String content = "";


        for (EditDataModel model : editDataModels) {
            //图片内容
            if (!TextUtils.isEmpty(model.getImagePath())) {
                String qiniuPath = "";
                for (EditDataImgModel imgModel : editDataImgModels) {
                    if (imgModel.getLocalPath().equals(model.getImagePath())) {
                        qiniuPath = imgModel.getQiniuPath();
                    }
                }
                content = content + "<img>" + qiniuPath + "</img>";
            }
            //文字内容
            else if (!TextUtils.isEmpty(model.getInputStr())) {
                content = content + "<p>" + model.getInputStr() + "</p>";
            }
        }

        PostService.getInstance().post(title, content, coverQiNiuPath, type);


    }


    private void sendPost() {
//        title = etNewpostTitle.getText().toString();
//        type = spinnerNewpostTags.getSelectedItemPosition();
    }

    private void initMultiImageSelector() {
        multiImageSelector = MultiImageUtil.getInstance().getMultiImageSelector(mSelectPath);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_newpost_camera:
                multiImageSelector.start(EditPostActivity.this, ConstantValue.REQUEST_IMAGE);
                break;
            case R.id.iv_newpost_img:
                isCover = true;
                multiImageSelector.start(EditPostActivity.this, ConstantValue.REQUEST_IMAGE);
                break;
            case R.id.tv_toolbar_send:
                initContentLayout();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantValue.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    if (isCover) {
                        IntentUtils.intent2CropActivity(this, mSelectPath.get(0), ConstantValue.CROP_TYPE_POSTIMG);
                    } else {
                        //做存储
                        editDataImgModels.add(new EditDataImgModel(mSelectPath.get(0), ""));
                        //上传到七牛
                        QiNiuUtil.doUpload(mSelectPath.get(0), ConstantValue.QINIU_BUCKETNAME_POST);
                    }
                }
            }
        }
    }

    @Subscribe
    public void onEvent(final ImageEvent.ImageCropEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(event.getImgPath())) {
                    //上传到七牛
                    QiNiuUtil.doUpload(event.getImgPath(), ConstantValue.QINIU_BUCKETNAME_POST);
                }
            }
        });
    }


    @Subscribe
    public void onEvent(final BaseEvent.BasicEvent event) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseModel baseModel = event.getBaseModel();
                if (event.getBasicEventEnum() == BasicEventEnum.POST_POST) {
                    if (ValidateUtil.hasError(baseModel)) {
                        showToast(baseModel.getError_text(), SuperToast.Duration.VERY_SHORT);
                    } else {
                        showToast("发布成功", SuperToast.Duration.VERY_SHORT);
                        finish();
                    }

                }

            }
        });

    }

    @Subscribe
    public void onEvent(final QiNiuEvent.UploadCompleteEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //封面
                if (isCover) {
                    etNewpostContent.getTvImg().setVisibility(View.GONE);
                    etNewpostContent.getTopImg().setVisibility(View.VISIBLE);
                    coverQiNiuPath = event.getQiniuPath();
                    ImageUtils.displayImage(EditPostActivity.this, "file://" + event.getLocalPath(), etNewpostContent.getTopImg());
                    isCover = false;
                    mSelectPath.clear();
                } else {
                    //插入编辑框
                    etNewpostContent.insertImage(event.getLocalPath());

                    //根据本地路径匹配七牛路径

                    for (int i = 0; i < editDataImgModels.size(); i++) {
                        if (editDataImgModels.get(i).getLocalPath().equals(event.getLocalPath())) {
                            if (TextUtils.isEmpty(editDataImgModels.get(i).getQiniuPath())) {
                                editDataImgModels.get(i).setQiniuPath(event.getQiniuPath());
                            }
                        }
                    }
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
