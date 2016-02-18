package com.htlc.cyjk.app.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.htlc.cyjk.R;

/**
 * Created by sks on 2016/2/17.
 */
public class PickPhotoDialog extends AlertDialog {

    public TextView textAlbum,textCamera,textCancel;
    public PickPhotoDialog(Context context, int theme) {
        super(context, theme);
    }

    public PickPhotoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pick_photo);
        textAlbum = (TextView) findViewById(R.id.textAlbum);
        textCamera = (TextView) findViewById(R.id.textCamera);
        textCancel = (TextView) findViewById(R.id.textCancel);
    }

}
