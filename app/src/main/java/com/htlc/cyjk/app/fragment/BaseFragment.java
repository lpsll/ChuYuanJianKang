package com.htlc.cyjk.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.htlc.cyjk.app.activity.BaseActivity;

/**
 * Created by sks on 2015/12/29.
 */
public class BaseFragment extends Fragment{
    protected BaseActivity baseActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
    }
    public boolean handleNetworkOnFailure(String errorEvent, String message){
        return baseActivity.handleNetworkOnFailure(errorEvent, message);
    }
}
