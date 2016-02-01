package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.util.CommonUtil;
import com.htlc.cyjk.app.util.LogUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/28.
 */
public class ThirdChildAdapter extends BaseAdapter{
    private Activity mActivity;
    private ArrayList mList;
    private boolean isDeleteState;

    public ThirdChildAdapter(ArrayList list, Activity activity) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.adapter_third_child_fragment, null);
            holder.linearItem = (LinearLayout) convertView.findViewById(R.id.linearItem);
            holder.linearUnit = (LinearLayout) convertView.findViewById(R.id.linearUnit);
            holder.imageSelect = (ImageView) convertView.findViewById(R.id.imageSelect);
            holder.textName = (TextView) convertView.findViewById(R.id.textName);
            holder.editCount = (EditText) convertView.findViewById(R.id.editCount);
            holder.textUnit = (TextView) convertView.findViewById(R.id.textUnit);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ViewHolder finalHolder = holder;
        holder.linearUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDeleteState()){
                    LogUtil.e(ThirdChildAdapter.this,"unit="+position);
                    showPopupWindow(v, finalHolder);
                }

            }
        });
        if(isDeleteState){
            holder.imageSelect.setVisibility(View.VISIBLE);
            holder.editCount.setEnabled(false);
            holder.editCount.setFocusable(false);
        }else {
            holder.imageSelect.setVisibility(View.GONE);
            holder.imageSelect.setSelected(false);
            holder.editCount.setEnabled(true);
            holder.editCount.setFocusable(true);
        }
        //具体数据处理
        return convertView;
    }
    class ViewHolder{
        LinearLayout linearItem, linearUnit;
        ImageView imageSelect;
        EditText editCount;
        TextView textName,textUnit;
    }

    public boolean isDeleteState() {
        return isDeleteState;
    }

    public void setDeleteState(boolean isDeleteState) {
        this.isDeleteState = isDeleteState;
    }

    private void showPopupWindow(View v, final ViewHolder finalHolder) {

        String[] units = CommonUtil.getResourceStringArray(R.array.fragment_third_child_adapter_unit);
        final ArrayList<String> unitList = new ArrayList<String>();
        String currentUnit = finalHolder.textUnit.getText().toString().trim();
        for(String unit : units){
            if(!currentUnit.equals(unit)){
                unitList.add(unit);
            }
        }
        // 一个自定义的布局，作为显示的内容
        ListView contentView = (ListView) LayoutInflater.from(mActivity).inflate(
                R.layout.pop_window_third_child_adapter, null);
        // 设置按钮的点击事件
        BaseAdapter adapter = new PopWindowThirdChildAdapter(unitList,mActivity);
        contentView.setAdapter(adapter);


        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        //int width = 148*displaymetrics.widthPixels/displaymetrics.widthPixels;
        int width = v.getWidth();
        final PopupWindow popupWindow = new PopupWindow(contentView,
                width, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.e(ThirdChildAdapter.this, "点击了单位："+ unitList.get(position));
                finalHolder.textUnit.setText(unitList.get(position));
                if(popupWindow != null){
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.edit_rectangle_shape));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(v, 0,-5);

    }
}
