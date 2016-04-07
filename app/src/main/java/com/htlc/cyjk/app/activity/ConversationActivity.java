package com.htlc.cyjk.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.htlc.cyjk.R;
import com.htlc.cyjk.core.ActionCallbackListener;

import java.util.Locale;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by sks on 2016/2/15.
 */
public class ConversationActivity extends BaseActivity{
    private View mImageBack;
    private TextView mTextTitle;
    /**
     * 目标 Id
     */
    private String mTargetId;
    private String mTitle;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent = getIntent();
        setupView();
        getIntentDate(intent);
        getPermission();
    }

    private void getPermission() {
        showProgressHUD();
        appAction.conversationPermission(mTargetId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                dismissProgressHUD();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                dismissProgressHUD();
                if (handleNetworkOnFailure(errorEvent, message)) return;
                showMessageDialog(message);
            }
        });
    }
    private void showMessageDialog(String message) {
        View view =  View.inflate(this,R.layout.dialog_message,null);
        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(message);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showTipsDialog(view, 300, 150, false);
    }

    private void setupView() {
        mTextTitle = (TextView) findViewById(R.id.textTitle);
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTitle = intent.getData().getQueryParameter("title");
        mTextTitle.setText(mTitle);
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型

        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }
}
