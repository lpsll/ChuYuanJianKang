package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.bean.FourthAdapterBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class PopWindowThirdChildAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public PopWindowThirdChildAdapter(ArrayList list, Activity activity) {
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.adapter_pop_window_third_child_fragment, null);
            holder.textUnit = (TextView) convertView.findViewById(R.id.textUnit);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String bean = (String) mList.get(position);
        //具体数据处理
        holder.textUnit.setText(bean);
        return convertView;
    }
    class ViewHolder{
        TextView textUnit;
    }
}
