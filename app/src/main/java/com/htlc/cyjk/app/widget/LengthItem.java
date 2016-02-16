package com.htlc.cyjk.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by sks on 2016/2/16.
 */
public class LengthItem extends AutoLinearLayout implements Checkable {
    private boolean mChecked;
    private CheckBox mCheckBox;
    private TextView mTextLength;
    private TextView mTextMoney;

    public LengthItem(Context context) {
        this(context, null);
    }
    public LengthItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addView(View.inflate(context, R.layout.adapter_length, null));
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mTextLength = (TextView) findViewById(R.id.textLength);
        mTextMoney = (TextView) findViewById(R.id.textMoney);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        mCheckBox.setChecked(mChecked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public TextView getTextMoney() {
        return mTextMoney;
    }

    public TextView getTextLength() {
        return mTextLength;
    }
}
