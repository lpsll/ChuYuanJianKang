package com.htlc.cyjk.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.widget.PickPhotoDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sks on 2016/2/15.
 */
public class PerfectInfoActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View mImageBack;

    private ImageView mImageHead;
    private RadioGroup mRadioGroup;
    private TextView mTextAddress, mTextButtonFinish;
    private LinearLayout mLinearJob;
    private RelativeLayout mRelativeDischargeSummary;
    private EditText mEditUsername, mEditAge;
    private PickPhotoDialog mPickPhotoDialog;

    private boolean isFemale;
    private File mImageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_info);
        setupView();
    }

    private void setupView() {
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditAge = (EditText) findViewById(R.id.editAge);

        mImageHead = (ImageView) findViewById(R.id.imageHead);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mLinearJob = (LinearLayout) findViewById(R.id.linearJob);
        mTextAddress = (TextView) findViewById(R.id.textAddress);
        mRelativeDischargeSummary = (RelativeLayout) findViewById(R.id.relativeDischargeSummary);
        mTextButtonFinish = (TextView) findViewById(R.id.textButtonFinish);


        mImageHead.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mLinearJob.setOnClickListener(this);
        mTextAddress.setOnClickListener(this);
        mRelativeDischargeSummary.setOnClickListener(this);
        mTextButtonFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageHead:
                LogUtil.e(this,"imageHead");
                showPickPhotoDialog();
                break;
            case R.id.linearJob:
                LogUtil.e(this,"linearJob");
                break;
            case R.id.textAddress:
                LogUtil.e(this,"textAddress");
                break;
            case R.id.relativeDischargeSummary:
                LogUtil.e(this,"relativeDischargeSummary");
                Intent intent = new Intent(this, DischargeSummaryActivity.class);
                startActivity(intent);
                break;
            case R.id.textButtonFinish:
                LogUtil.e(this,"textButtonFinish");
                commit();
                break;
        }
    }
    

    /**
     * 提交数据
     */
    private void commit() {

    }

    /**
     * 性别选择
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.radioButton0){
            isFemale = false;
        }
        if(checkedId==R.id.radioButton1){
            isFemale = true;
        }
    }


    /**
     * 选择图片对话框
     */
    private void showPickPhotoDialog() {

        mPickPhotoDialog = new PickPhotoDialog(this, R.style.TransparentAlertDialog);//创建Dialog并设置样式主题
        mPickPhotoDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        mPickPhotoDialog.show();
        mPickPhotoDialog.textAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
                pickPhotoByAlbum();

            }
        });
        mPickPhotoDialog.textCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
                takePhoto();
            }
        });
        mPickPhotoDialog.textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
            }
        });

        Window win = mPickPhotoDialog.getWindow();

        WindowManager.LayoutParams params = win.getAttributes();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

    }
    private void takePhoto() {

        /**
         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
         * 官方文档太长了就不看了，其实是错的
         */
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mImageFile = new File(Environment.getExternalStorageDirectory(), "IMG_" + filename + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        startActivityForResult(intent, 2);

    }
    private void pickPhotoByAlbum() {
        /**
         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
         */
        Intent intent = new Intent(Intent.ACTION_PICK, null);


        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images
         * .Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
         * ："image/jpeg 、 image/png等的类型"
         */
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if(Activity.RESULT_OK != resultCode) return;
                startPhotoZoom(data.getData());
                break;
            // 如果是调用相机拍照时
            case 2:
                if(Activity.RESULT_OK != resultCode) return;
                startPhotoZoom(Uri.fromFile(mImageFile));
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
                 *
                 */
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */


    public void startPhotoZoom(Uri uri) {
 /*
 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
 * yourself_sdk_path/docs/reference/android/content/Intent.html
 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        // 下面这句指定调用相机拍照后的照片存储的路径
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mImageFile = new File(Environment.getExternalStorageDirectory(), "IMG_" + filename + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));

        startActivityForResult(intent, 3);
    }
    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            final Bitmap photo = extras.getParcelable("data");
            mImageHead.setImageBitmap(photo);
        }

    }
}
