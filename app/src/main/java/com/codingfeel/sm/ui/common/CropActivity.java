package com.codingfeel.sm.ui.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ConstantValue;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.event.ImageEvent;
import com.codingfeel.sm.ui.ToolBarActivity;
import com.codingfeel.sm.utils.BitmapUtil;
import com.codingfeel.sm.utils.ImageUtils;
import com.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heboot on 16/7/24.
 */
public class CropActivity extends ToolBarActivity implements View.OnClickListener {


    @BindView(R.id.crop_iv)
    CropImageView cropIv;
    @BindView(R.id.tv_crop_ok)
    TextView tvCropOk;
    @BindView(R.id.tv_crop_cancel)
    TextView tvCropCancel;
    @BindView(R.id.llyt_crop_bottom)
    LinearLayout llytCropBottom;


    private String imagePath;
    private String cropType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        showToolBar(getResources().getString(R.string.page_title_crop), true, null);

        imagePath = getIntent().getExtras().getString(ContentKey.IMAGE_PATH);

        cropType = getIntent().getExtras().getString(ContentKey.CROP_TYPE);

        if (cropType.equals(ConstantValue.CROP_TYPE_HEAD)) {
            cropIv.setAspectRatio(1, 1);
        } else if (cropType.equals(ConstantValue.CROP_TYPE_POSTIMG)) {
            cropIv.setAspectRatio(2, 1);
        }


        cropIv.setFixedAspectRatio(true);

        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath);

        cropIv.setImageBitmap(bitmap);

        initListener();

    }


    private void initListener() {
        tvCropOk.setOnClickListener(this);
        tvCropCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_crop_ok:
                Bitmap bitmap = cropIv.getCroppedImage();
                String path = "";
                if (cropType.equals(ConstantValue.CROP_TYPE_HEAD)) {
                    path = BitmapUtil.saveBitmapFile(bitmap, true);
                } else if (cropType.equals(ConstantValue.CROP_TYPE_POSTIMG)) {
                    path = BitmapUtil.saveBitmapFile(bitmap);
                }
                EventBus.getDefault().post(new ImageEvent.ImageCropEvent(path));
                finish();
                break;
            case R.id.tv_crop_cancel:
                finish();
                break;
        }
    }
}
