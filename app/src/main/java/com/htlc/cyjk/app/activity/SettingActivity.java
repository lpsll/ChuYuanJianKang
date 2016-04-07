package com.htlc.cyjk.app.activity;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.FourthAdapter;
import com.htlc.cyjk.app.bean.FourthAdapterBean;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import io.rong.imkit.RongIM;

/**
 * Created by sks on 2016/2/15.
 */
public class SettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private int[] itemImageIds;
    private String[] itemNames;
    private boolean[] itemHaveEmptys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupView();
    }
    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new FourthAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
//        itemImageIds = new int[]{R.mipmap.activity_setting_1, R.mipmap.activity_setting_2, R.mipmap.activity_setting_3};
//        itemNames = CommonUtil.getResourceStringArray(R.array.activity_setting_children);
//        itemHaveEmptys = new boolean[]{true,false,true};
        itemImageIds = new int[]{R.mipmap.activity_setting_1, R.mipmap.activity_setting_3};
        itemNames = CommonUtil.getResourceStringArray(R.array.activity_setting_children);
        itemHaveEmptys = new boolean[]{true,true};
        for(int i=0; i<itemImageIds.length;i++){
            FourthAdapterBean bean = new FourthAdapterBean();
            bean.imageId = itemImageIds[i];
            bean.name = itemNames[i];
            bean.haveEmpty = itemHaveEmptys[i];
            mList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FourthAdapterBean bean = (FourthAdapterBean) mList.get(position);
        LogUtil.e(this, bean.name);
        switch (position){
            case 0:
                clearCache();
                break;
            case 1:
                exist();
                break;
            case 2:

                break;
        }
    }
    private void clearCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");//设置对话框标题
        builder.setMessage("你是否清除缓存？");//设置显示的内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                LogUtil.e(SettingActivity.this, "清除缓存");
                ToastUtil.showToast(SettingActivity.this, "清除缓存成功！");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }
    private void exist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");//设置对话框标题
        builder.setMessage("你是否确认退出？");//设置显示的内容

        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                logout();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(CommonUtil.getResourceColor(R.color.text_blue));
    }
    private void logout() {
        application.clearUserBean();
        LoginActivity.start(this,null);
        finish();
    }
}
