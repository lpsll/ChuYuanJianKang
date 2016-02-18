package com.htlc.cyjk.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.SecondAdapter;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.widget.SideBar;
import com.htlc.cyjk.model.ContactBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/27.
 */
public class ContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private PullToRefreshListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextDialog;
    private SideBar mSideBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        setupView(view);

        return view;
    }

    private void setupView(View view) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.listView);
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
                LogUtil.e(ContactFragment.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
                if (position != -1) {
                    mListView.getRefreshableView().setSelection(position+1);
                }
            }
        });
        initData();
    }

    private void initData() {
        String name = "Larno";
        String[] type = {"A", "B", "C"};
        for (int i = 0; i < 20; i++) {
            ContactBean bean = new ContactBean();
            bean.name = name;
            bean.type = type[0];
            mList.add(bean);
        }
        for (int i = 0; i < 10; i++) {
            ContactBean bean = new ContactBean();
            bean.name = name;
            bean.type = type[1];
            mList.add(bean);
        }
        for (int i = 0; i < 15; i++) {
            ContactBean bean = new ContactBean();
            bean.name = name;
            bean.type = type[2];
            mList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void getMoreData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
