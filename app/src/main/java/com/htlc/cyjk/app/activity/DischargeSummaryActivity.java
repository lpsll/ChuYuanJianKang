package com.htlc.cyjk.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.util.DateFormat;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.core.ActionCallbackListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sks on 2016/2/15.
 */
public class DischargeSummaryActivity extends BaseActivity implements View.OnClickListener {
    public static final String Name = "Name";
    public static final String Age = "Age";
    public static final String Sex = "Sex";
    public static final String Job = "Job";
    private boolean isFemale;
    private String name;
    private String age;
    private String job;

    private EditText mEditName, mEditAge, mEditJob;
    private RadioButton mRadioButton0, mRadioButton1;
    private TextView mTextStartTime, mTextEndTime, mTextTotalTime, mTextButtonFinish;
    private EditText mEditInDiagnosis, mEditOutDiagnosis, mEditEffect, mEditSpecialItem,
            mEditInStatus, mEditAtStatus, mEditOutStatus, mEditOutConsideration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFemale = getIntent().getBooleanExtra(Sex, false);
        name = getIntent().getStringExtra(Name);
        age = getIntent().getStringExtra(Age);
        job = getIntent().getStringExtra(Job);
        setContentView(R.layout.activity_discharge_summary);

        setupView();
    }

    private void setupView() {
        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditName = (EditText) findViewById(R.id.editName);
        mEditAge = (EditText) findViewById(R.id.editAge);
        mEditJob = (EditText) findViewById(R.id.editJob);
        mRadioButton0 = (RadioButton) findViewById(R.id.radioButton0);
        mRadioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        if (!TextUtils.isEmpty(name)) {
            mEditName.setText(name);
        }
        if (!TextUtils.isEmpty(age)) {
            mEditAge.setText(age);
        }
        if (!TextUtils.isEmpty(job)) {
            mEditJob.setText(job);
        }
        mRadioButton0.setChecked(!isFemale);
        mRadioButton1.setChecked(isFemale);

        mTextStartTime = (TextView) findViewById(R.id.textStartTime);
        mTextStartTime.setText(DateFormat.getTimeByDay(new Date(System.currentTimeMillis())));
        mTextEndTime = (TextView) findViewById(R.id.textEndTime);
        mTextEndTime.setText(DateFormat.getTimeByDay(new Date(System.currentTimeMillis())));
        mTextTotalTime = (TextView) findViewById(R.id.textTotalTime);
        mTextTotalTime.setText("1");
        mTextButtonFinish = (TextView) findViewById(R.id.textButtonFinish);
        mTextStartTime.setOnClickListener(this);
        mTextEndTime.setOnClickListener(this);
        mTextButtonFinish.setOnClickListener(this);

        mEditInDiagnosis = (EditText) findViewById(R.id.editInDiagnosis);
        mEditOutDiagnosis = (EditText) findViewById(R.id.editOutDiagnosis);
        mEditInStatus = (EditText) findViewById(R.id.editInStatus);
        mEditAtStatus = (EditText) findViewById(R.id.editAtStatus);
        mEditOutStatus = (EditText) findViewById(R.id.editOutStatus);
        mEditOutConsideration = (EditText) findViewById(R.id.editOutConsideration);
        mEditEffect = (EditText) findViewById(R.id.editEffect);
        mEditSpecialItem = (EditText) findViewById(R.id.editSpecialItem);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textStartTime:
                textTime(true);
                break;
            case R.id.textEndTime:
                textTime(false);
                break;
            case R.id.textButtonFinish:
                commit();
                break;
        }
    }

    private void commit() {
        String userId = application.getUserBean().userid;
        String startTime = mTextStartTime.getText().toString();
        String endTime = mTextEndTime.getText().toString();
        String totalTime = mTextTotalTime.getText().toString();
        String inDiagnose = mEditInDiagnosis.getText().toString().trim();
        String outDiagnose = mEditOutDiagnosis.getText().toString().trim();
        String effect = mEditEffect.getText().toString().trim();
        String specialItem = mEditSpecialItem.getText().toString().trim();
        String inStatus = mEditInStatus.getText().toString().trim();
        String atStatus = mEditAtStatus.getText().toString().trim();
        String outStatus = mEditOutStatus.getText().toString().trim();
        String outAdvice = mEditOutConsideration.getText().toString().trim();
        appAction.dischargeSummary(userId, startTime, endTime, totalTime,
                inDiagnose, outDiagnose, effect, specialItem,
                inStatus, atStatus, outStatus, outAdvice,"", new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        ToastUtil.showToast(DischargeSummaryActivity.this,"提交成功");
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        if(handleNetworkOnFailure(errorEvent, message)) return;
                        ToastUtil.showToast(DischargeSummaryActivity.this,message);
                    }
                });
    }

    private void textTime(final boolean isStart) {
        //时间选择器
        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(true);
        timePickerView.setCancelable(true);
        //时间选择后回调
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if(isStart){
                    String endTime = mTextEndTime.getText().toString();
                    String startTime = DateFormat.getTimeByDay(date);
                    long totalTime = DateFormat.getDays(startTime, endTime);
                    if(totalTime >= 0) {
                        totalTime++;
                        mTextStartTime.setText(startTime);
                        mTextTotalTime.setText(totalTime+"");
                    }
                }else {
                    String startTime = mTextStartTime.getText().toString();
                    String endTime = DateFormat.getTimeByDay(date);
                    long totalTime = DateFormat.getDays(startTime, endTime);
                    if(totalTime >= 0) {
                        totalTime++;
                        mTextEndTime.setText(endTime);
                        mTextTotalTime.setText(totalTime+"");
                    }
                }

            }
        });
        timePickerView.show();
    }
}
