package com.htlc.cyjk.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by sks on 2016/2/16.
 */
public class OrderItem extends AutoLinearLayout{
    public EditText editName,editPrice,editUnit,editNumber;
    public View imageDelete;

    public OrderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addView(View.inflate(context, R.layout.layout_order_item, null));
        editName = (EditText) findViewById(R.id.editName);
        editPrice = (EditText) findViewById(R.id.editPrice);
        editUnit = (EditText) findViewById(R.id.editUnit);
        editNumber = (EditText) findViewById(R.id.editNumber);
        imageDelete = findViewById(R.id.imageDelete);
    }


}
