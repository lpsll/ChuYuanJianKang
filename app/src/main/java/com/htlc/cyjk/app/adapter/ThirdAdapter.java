package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.model.InformationBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class ThirdAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public ThirdAdapter(ArrayList list, Activity activity) {
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
            convertView = View.inflate(mActivity, R.layout.adapter_third_fragment, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.textView = (TextView) convertView.findViewById(R.id.textTitle);
            holder.textDes = (TextView) convertView.findViewById(R.id.textDes);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        InformationBean bean = (InformationBean) mList.get(position);
        ImageLoader.getInstance().displayImage(bean.img,holder.imageView);
        holder.textView.setText(bean.title);
        holder.textDes.setText(bean.introduce);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView,textDes;
    }
}
