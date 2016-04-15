package com.htlc.cyjk.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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
import com.htlc.cyjk.app.util.Constant;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.model.DrugBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sks on 2016/1/28.
 */
public class ThirdChildAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList mList;


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
        if (convertView == null) {
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
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.editCount.setTag(position);
        }
        //具体数据处理
        DrugBean bean = (DrugBean) mList.get(position);
        bean.position = position;
        holder.textName.setText(bean.medicine);
        if (!TextUtils.isEmpty(bean.unit)) {
            holder.textUnit.setText(bean.unit);
        }
        final ViewHolder finalHolder = holder;
        holder.linearUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, finalHolder, position);
            }
        });
        finalHolder.editCount.setText(bean.num);
        finalHolder.editCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;
                if (hasFocus) {
                    editText.setText("");
                } else {
                    if (mList.size() > position) {
                        String temp = editText.getText().toString();
                        if(TextUtils.isEmpty(temp)){
                            editText.setText("1");
                            ((DrugBean) mList.get(position)).num = "1";
                        }else {
                            int tempNum = Integer.parseInt(temp);
                            if(tempNum == 0){
                                editText.setText("1");
                                ((DrugBean) mList.get(position)).num = "1";
                            }else {
                                editText.setText(tempNum+"");
                                ((DrugBean) mList.get(position)).num = tempNum+"";
                            }
                        }
                    }
                }
            }
        });
        holder.imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout linearItem, linearUnit;
        ImageView imageSelect;
        EditText editCount;
        TextView textName, textUnit;
    }

    private void showPopupWindow(View v, final ViewHolder finalHolder, final int beanPosition) {

        String[] units = CommonUtil.getResourceStringArray(R.array.fragment_third_child_adapter_unit);
        final ArrayList<String> unitList = new ArrayList<String>();
        String currentUnit = finalHolder.textUnit.getText().toString().trim();
        for (String unit : units) {
            if (!currentUnit.equals(unit)) {
                unitList.add(unit);
            }
        }
        // 一个自定义的布局，作为显示的内容
        ListView contentView = (ListView) LayoutInflater.from(mActivity).inflate(
                R.layout.pop_window_third_child_adapter, null);
        // 设置按钮的点击事件
        BaseAdapter adapter = new PopWindowThirdChildAdapter(unitList, mActivity);
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
                LogUtil.e(ThirdChildAdapter.this, "点击了单位：" + unitList.get(position));
                finalHolder.textUnit.setText(unitList.get(position));
                ((DrugBean) (mList.get(beanPosition))).unit = unitList.get(position);
                if (popupWindow != null) {
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
        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay();
        int padding = 5 * d.getHeight() / Constant.ScreenHeight;
        popupWindow.showAsDropDown(v, 0, padding);

    }
}
