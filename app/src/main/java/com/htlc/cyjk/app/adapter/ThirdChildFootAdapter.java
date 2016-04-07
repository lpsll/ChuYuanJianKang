package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.model.MedicalHistoryItemBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class ThirdChildFootAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;

    public ThirdChildFootAdapter(ArrayList list, Activity activity) {
        this.mList = list;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.e(this,"mlist.size="+mList.size());
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.adapter_third_child_foot_fragment, null);
            holder.textTime = (TextView) convertView.findViewById(R.id.textTime);
            holder.textDrugName = (TextView) convertView.findViewById(R.id.textDrugName);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //具体数据处理
        MedicalHistoryItemBean bean = (MedicalHistoryItemBean) mList.get(position);
        holder.textTime.setText(bean.date);
        holder.textDrugName.setText(bean.drug.get(0).medicine);
        return convertView;
    }
    class ViewHolder{
        TextView textTime,textDrugName;
    }
}
