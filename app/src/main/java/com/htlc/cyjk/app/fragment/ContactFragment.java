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

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.htlc.cyjk.R;
import com.htlc.cyjk.app.adapter.SecondAdapter;
import com.htlc.cyjk.app.util.LogUtil;
import com.htlc.cyjk.app.util.ToastUtil;
import com.htlc.cyjk.app.widget.SideBar;
import com.htlc.cyjk.core.ActionCallbackListener;
import com.htlc.cyjk.model.ContactBean;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by sks on 2016/1/27.
 */
public class ContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayList mList = new ArrayList();
    private BaseAdapter mAdapter;
    private TextView mTextDialog;
    private SideBar mSideBar;
    private View mView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
    public void onEventMainThread(ContactBean event) {
        String msg = "onEventMainThread收到了消息：";
        getContactList();
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
                LogUtil.e(ContactFragment.this, "onTouchingLetterChanged---position=?" + position + ";---type=?" + s);
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
//                ToastUtil.showToast(getActivity(), message);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactBean bean = (ContactBean) mList.get(position);
        if (RongIM.getInstance() != null) {
            /**
             * 启动单聊界面。
             *
             * @param context      应用上下文。
             * @param targetUserId 要与之聊天的用户 Id。
             * @param title        聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
             */
            RongIM.getInstance().startPrivateChat(getActivity(), bean.userid, bean.name);
        }
    }
}
