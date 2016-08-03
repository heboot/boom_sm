package com.codingfeel.sm.ui.post;

import android.content.Intent;
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
import com.codingfeel.sm.enums.BasicEventEnum;
import com.codingfeel.sm.event.BaseEvent;
import com.codingfeel.sm.event.ImageEvent;
import com.codingfeel.sm.event.QiNiuEvent;
import com.codingfeel.sm.model.BaseModel;
import com.codingfeel.sm.model.EditDataImgModel;
import com.codingfeel.sm.model.EditDataModel;
import com.codingfeel.sm.service.PostService;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.BitmapUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.codingfeel.sm.utils.IntentUtils;
import com.codingfeel.sm.utils.MultiImageUtil;
import com.codingfeel.sm.utils.QiNiuUtil;
import com.codingfeel.sm.utils.ValidateUtil;
import com.codingfeel.sm.views.richeditor.RichTextEditor;
import com.github.johnpersano.supertoasts.SuperToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Heboot on 16/7/18.
 */
public class NewPostActivity extends ToolBarActivity implements View.OnClickListener {

    //    @BindView(R.id.spinner_newpost_tags)
//    AppCompatSpinner spinnerNewpostTags;
//    @BindView(R.id.iv_newpost_img)
//    ImageView ivNewpostImg;
    @BindView(R.id.et_newpost_content)
    RichTextEditor etNewpostContent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        showToolBar(getResources().getString(R.string.page_title_new_post), true, null);
        showToolBarSend(this);
        initMultiImageSelector();
        initListener();
    }


    private void initListener() {
        ivNewpostCamera.setOnClickListener(this);
//        ivNewpostImg.setOnClickListener(this);
        etNewpostContent.initTopSpinner(this, MyApplication.getInstance().getPostTagModelList());
        etNewpostContent.initImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCover = true;
                multiImageSelector.start(NewPostActivity.this, ConstantValue.REQUEST_IMAGE);
            }
        });
        etNewpostContent.initTvImgListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCover = true;
                multiImageSelector.start(NewPostActivity.this, ConstantValue.REQUEST_IMAGE);
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
                content = content + "<img src='" + qiniuPath + "' />";
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
                multiImageSelector.start(NewPostActivity.this, ConstantValue.REQUEST_IMAGE);
                break;
            case R.id.iv_newpost_img:
                isCover = true;
                multiImageSelector.start(NewPostActivity.this, ConstantValue.REQUEST_IMAGE);
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
                        //拿到选择图片的bitmap
                        String path = BitmapUtil.saveBitmapFile(ImageUtils.getSmallBitmap(mSelectPath.get(0)));
                        //做存储
                        editDataImgModels.add(new EditDataImgModel(path, ""));
                        //上传到七牛
                        QiNiuUtil.doUpload(path, ConstantValue.QINIU_BUCKETNAME_POST);
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
                    ImageUtils.displayImage(NewPostActivity.this, "file://" + event.getLocalPath(), etNewpostContent.getTopImg());
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
