package com.htlc.cyjk.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by sks on 2016/2/16.
 */
public class PayItem extends AutoLinearLayout implements Checkable {
    private boolean mChecked;
    private CheckBox mCheckBox;
    private TextView mTextPayName;
    private ImageView mImagePayIcon;

    public PayItem(Context context) {
        this(context, null);
    }
    public PayItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addView(View.inflate(context, R.layout.adapter_pay, null));
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mTextPayName = (TextView) findViewById(R.id.textPayName);
        mImagePayIcon = (ImageView) findViewById(R.id.imagePayIcon);
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

    public TextView getTextPayName() {
        return mTextPayName;
    }

    public ImageView getImagePayIcon() {
        return mImagePayIcon;
    }
}
