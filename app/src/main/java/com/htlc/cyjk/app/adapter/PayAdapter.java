package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.bean.FourthAdapterBean;
import com.htlc.cyjk.app.bean.PaymentBean;
import com.htlc.cyjk.app.widget.PayItem;
import com.htlc.cyjk.model.PriceBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class PayAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList mList;

    public PayAdapter(ArrayList list, Activity activity) {
        this.mList = list;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PayItem view = (PayItem) View.inflate(mActivity, R.layout.adapter_pay_item, null);
        //对于listview，注意添加这一行，即可在item上使用高度
        AutoUtils.autoSize(view);

        //具体数据处理
        PaymentBean bean = (PaymentBean) mList.get(position);
        view.getImagePayIcon().setImageResource(bean.logoId);
        view.getTextPayName().setText(bean.payName);
        return view;
    }
}
