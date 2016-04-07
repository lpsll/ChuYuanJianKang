package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.bean.FourthAdapterBean;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.model.ContactBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class SecondAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public SecondAdapter(ArrayList list, Activity activity) {
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
            convertView = View.inflate(mActivity, R.layout.adapter_second_fragment, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.textType = (TextView) convertView.findViewById(R.id.textType);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        ContactBean bean = (ContactBean) mList.get(position);
//        holder.imageView.setImageResource(bean.imageId);
        holder.textType.setText(bean.head);
        LogUtil.e(this,"首字母："+bean.head);
        holder.textView.setText(bean.name);
        ImageLoader.getInstance().displayImage(bean.photo,holder.imageView);
        if(position == 0){
            holder.textType.setVisibility(View.VISIBLE);
        }else {
            String temp = ((ContactBean)mList.get(position-1)).head;
            if(temp.equals(bean.head)){
                holder.textType.setVisibility(View.GONE);
            }else {
                holder.textType.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView, textType;
    }

    public int getFirstPositionOfType(String type){
        for (int i = 0; i < getCount(); i++) {
            String tempType = ((ContactBean)mList.get(i)).head;
            if (tempType.equals(type)) {
                return i;
            }
        }
        return -1;
    }
}
