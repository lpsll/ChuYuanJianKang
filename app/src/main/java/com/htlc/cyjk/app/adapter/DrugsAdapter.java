package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.model.ContactBean;
import com.htlc.cyjk.model.DrugBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class DrugsAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public DrugsAdapter(ArrayList list, Activity activity) {
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
            convertView = View.inflate(mActivity, R.layout.adapter_drugs, null);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.textType = (TextView) convertView.findViewById(R.id.textType);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        DrugBean bean = (DrugBean) mList.get(position);
        holder.textType.setText(bean.group);
        holder.textView.setText(bean.medicine);
        if(position == 0){
            holder.textType.setVisibility(View.VISIBLE);
        }else {
            String temp = ((DrugBean)mList.get(position-1)).group;
            if(temp.equals(bean.group)){
                holder.textType.setVisibility(View.GONE);
            }else {
                holder.textType.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
    class ViewHolder{
        TextView textView, textType;
    }

    public int getFirstPositionOfType(String type){
        for (int i = 0; i < getCount(); i++) {
            String tempType = ((DrugBean)mList.get(i)).group;
            if (tempType.equals(type)) {
                return i;
            }
        }
        return -1;
    }
}
