package com.htlc.cyjk.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.SecondAdapter;
import com.htlc.cyjk.app.event.SelectContactEvent;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.widget.SideBar;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.ContactBean;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by sks on 2016/1/27.
 */
public class SelectContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextDialog;
    private SideBar mSideBar;
    private View mView;
    private boolean isSelect = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null){
            mView= inflater.inflate(R.layout.fragment_contact, null);
            setupView(mView);
        }
        return mView;
    }

    private void setupView(View view) {
        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new SecondAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mTextDialog = (TextView) view.findViewById(R.id.textDialog);
        mSideBar = (SideBar) view.findViewById(R.id.sideBar);
        mSideBar.setTextView(mTextDialog);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = ((SecondAdapter) mAdapter).getFirstPositionOfType(s);
                LogUtil.e(SelectContactFragment.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
                if (position != -1) {
                    mListView.setSelection(position + 1);
                }
            }
        });
        initData();
    }

    private void initData() {
        if(baseActivity.application.getContactList().size()>0){
            mList.clear();
            mList.addAll(baseActivity.application.getContactList());
            mAdapter.notifyDataSetChanged();
        }else {
            getContactList();
        }
    }

    private void getContactList() {
        String userId = baseActivity.application.getUserBean().userid;
        baseActivity.appAction.contactList(userId, new ActionCallbackListener<ArrayList<ContactBean>>() {
            @Override
            public void onSuccess(ArrayList<ContactBean> data) {
                baseActivity.application.setContactList(data);
                mList.clear();
                mList.addAll(baseActivity.application.getContactList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isSelect){
            isSelect = true;
            ContactBean bean = (ContactBean) mList.get(position);
            SelectContactEvent event = new SelectContactEvent();
            event.bean = bean;
            EventBus.getDefault().post(event);
            getActivity().finish();
        }

    }
}
