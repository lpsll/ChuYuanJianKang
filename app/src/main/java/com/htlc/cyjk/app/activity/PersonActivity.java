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
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.bean.CityBean;
import com.htlc.cyjk.app.db.ProvinceDao;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.app.widget.PickPhotoDialog;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.PersonBean;
import com.htlc.cyjk.model.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sks on 2016/2/15.
 */
public class PersonActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextLeft, mTextRight;
    private View mImageBack;
    private ImageView mImageHead;
    private EditText mEditGender, mEditDepartment, mEditDoctor, mEditUsername, mEditAddress;
    private EditText mEditName, mEditAge;
    private LinearLayout mLinearDischargeSummary;
    private boolean isEditable = false;

    private PickPhotoDialog mPickPhotoDialog;
    private File mImageFile;

    private OptionsPickerView mPickViePwOptions;
    private ArrayList<CityBean> provinces = new ArrayList<CityBean>();
    private ArrayList<CityBean> citys = new ArrayList<CityBean>();
    private ArrayList<CityBean> countys = new ArrayList<CityBean>();
    private String mAddress;
    private String mAddressId;
    private PersonBean mPersonBean;

    private ArrayList<String> genders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String usernameCurrent = mEditUsername.getText().toString();
        if(!usernameCurrent.equals(application.getUserBean().username)){
            mEditUsername.setText(application.getUserBean().username);
        }
    }

    private void setupView() {
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextLeft = (TextView) findViewById(R.id.textLeft);
        mTextLeft.setOnClickListener(this);
        mTextRight = (TextView) findViewById(R.id.textRight);
        mTextRight.setOnClickListener(this);
        mImageHead = (ImageView) findViewById(R.id.imageHead);
        mImageHead.setOnClickListener(this);

        mEditName = (EditText) findViewById(R.id.editName);
        mEditAge = (EditText) findViewById(R.id.editAge);

        mEditDepartment = (EditText) findViewById(R.id.editDepartment);
        mEditDoctor = (EditText) findViewById(R.id.editDoctor);

        mEditGender = (EditText) findViewById(R.id.editGender);
        mEditGender.setOnClickListener(this);
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditUsername.setOnClickListener(this);
        mEditAddress = (EditText) findViewById(R.id.editAddress);
        mEditAddress.setOnClickListener(this);
        mLinearDischargeSummary = (LinearLayout) findViewById(R.id.linearDischargeSummary);
        mLinearDischargeSummary.setOnClickListener(this);

        refreshView();
        getPersonInfo();
    }

    private void getPersonInfo() {
        String userId = application.getUserBean().userid;
        LogUtil.e(this, "userId:" + userId);
        appAction.getPersonInfo(userId, new ActionCallbackListener<PersonBean>() {
            @Override
            public void onSuccess(PersonBean data) {
                mPersonBean = data;
                refreshView();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
    }

    private void refreshView() {
        UserBean bean = application.getUserBean();
        ImageLoader.getInstance().displayImage(bean.photo, mImageHead);
        mEditName.setText(bean.name);
        mEditUsername.setText(bean.username);
        if (mPersonBean != null) {
            mEditName.setText(mPersonBean.name);
            if ("1".equals(mPersonBean.sex)) {
                mEditGender.setText("女");
            } else {
                mEditGender.setText("男");
            }
            mEditDepartment.setText(mPersonBean.subjectName);
            mEditDoctor.setText(mPersonBean.doctor);
            mEditAddress.setText(mPersonBean.region);
            mEditAge.setText(mPersonBean.age);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textLeft:
                changeEditStatus();
                break;
            case R.id.textRight:
                if(isEditable){
                    postPersonInfo();
                }else {
                    changeEditStatus();
                }
                break;
            case R.id.imageHead:
                showPickPhotoDialog();
                break;
            case R.id.editGender:
                selectGender();
                break;
            case R.id.editUsername:
                goChangeUsername();
                break;
            case R.id.editAddress:
                selectAddress();
                break;
            case R.id.linearDischargeSummary:
                goDischargeSummary();
                break;
        }
    }

    private void goDischargeSummary() {
        if (isEditable) return;
        String name = mEditName.getText().toString().trim();
        String age = mEditAge.getText().toString().trim();
        String sex = mEditGender.getText().toString().trim();
        Intent intent = new Intent(this, DischargeSummaryActivity2.class);
        intent.putExtra(DischargeSummaryActivity2.Name, name);
        intent.putExtra(DischargeSummaryActivity2.Age, age);
        if("女".equals(sex)){
            intent.putExtra(DischargeSummaryActivity2.Sex, true);
        }
        startActivity(intent);
    }

    private void goChangeUsername() {
        Intent intent = new Intent(this, ChangeUsernameActivity.class);
        startActivity(intent);
    }

    private void selectAddress() {
        if (!isEditable) return;
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        provinces.clear();
        citys.clear();
        countys.clear();
        provinces.addAll(new ProvinceDao().getProvinces());
        citys.addAll(new ProvinceDao().getCities(provinces.get(0).area_code));
        countys.addAll(new ProvinceDao().getCounties(citys.get(0).area_code));

        //三级不联动效果  false
        mPickViePwOptions.setPicker(provinces, citys, countys, false);
        mPickViePwOptions.getWheelOptions().getWv_option1().setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ArrayList<CityBean> temp = new ProvinceDao().getCities(provinces.get(index).area_code);
                ArrayWheelAdapter adapter = new ArrayWheelAdapter(temp);
                citys.clear();
                citys.addAll(temp);
                mPickViePwOptions.getWheelOptions().getWv_option2().setAdapter(adapter);

                if (citys.size() == 0) {
                    mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(new ArrayWheelAdapter(new ArrayList<CityBean>()));
                    return;
                }
                ArrayList<CityBean> temp1 = new ProvinceDao().getCounties(citys.get(0).area_code);
                ArrayWheelAdapter adapter1 = new ArrayWheelAdapter(temp1);
                countys.clear();
                countys.addAll(temp1);
                mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(adapter1);
                mPickViePwOptions.setSelectOptions(index, 0, 0);

            }
        });
        mPickViePwOptions.getWheelOptions().getWv_option2().setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                ArrayList<CityBean> temp = new ProvinceDao().getCounties(citys.get(index).area_code);
                ArrayWheelAdapter adapter = new ArrayWheelAdapter(temp);
                countys.clear();
                countys.addAll(temp);
                mPickViePwOptions.getWheelOptions().getWv_option3().setAdapter(adapter);
            }
        });
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
//        mPickViePwOptions.setTitle("选择城市");
        mPickViePwOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0, 0, 0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                mAddress = provinces.get(options1).area_name + citys.get(option2).area_name + countys.get(options3).area_name;
                mEditAddress.setText(mAddress);
                mAddressId = countys.get(options3).area_code+"";
            }
        });
        mPickViePwOptions.show();
    }

    private void selectGender() {
        if (!isEditable) return;
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        genders.clear();
        String[] genderArray = {"男", "女"};
        for (int i = 0; i < genderArray.length; i++) {
            genders.add(genderArray[i]);
        }
        //三级不联动效果  false
        mPickViePwOptions.setPicker(genders);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                mEditGender.setText(genders.get(options1));
            }
        });
        mPickViePwOptions.show();

    }

    /**
     * 保存，修改，取消
     */
    private void changeEditStatus() {
        if (isEditable) {
            mEditName.setEnabled(false);
            mEditAge.setEnabled(false);

            mTextRight.setText("修改");
            mTextLeft.setVisibility(View.GONE);
            mImageBack.setVisibility(View.VISIBLE);
            refreshView();
        } else {
            mEditName.setEnabled(true);
            mEditName.setFocusable(true);
            mEditName.setFocusableInTouchMode(true);
            mEditName.requestFocus();

            mEditAge.setEnabled(true);
            mEditAge.setFocusable(true);
            mEditAge.setFocusableInTouchMode(true);
            mEditAge.requestFocus();

            mTextRight.setText("保存");
            mTextLeft.setVisibility(View.VISIBLE);
            mImageBack.setVisibility(View.GONE);
        }
        isEditable = !isEditable;
    }

    private void postPersonInfo() {
        UserBean bean = application.getUserBean();
        String sex = "0";
        if ("女".equals(mEditGender.getText().toString())) {
            sex = "1";
        }
        final String name = mEditName.getText().toString().trim();
        final String age = mEditAge.getText().toString();
        showProgressHUD();
        appAction.postPersonInfo(bean.userid, name, sex, age, mAddressId, mImageFile, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                dismissProgressHUD();
                getPersonInfo();
                changeEditStatus();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                dismissProgressHUD();
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /**
     * 选择图片对话框
     */
    private void showPickPhotoDialog() {
        if (!isEditable) return;
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
                if (Activity.RESULT_OK != resultCode) return;
                startPhotoZoom(data.getData());
                break;
            // 如果是调用相机拍照时
            case 2:
                if (Activity.RESULT_OK != resultCode) return;
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
