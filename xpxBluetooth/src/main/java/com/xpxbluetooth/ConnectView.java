package com.xpxbluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import intersky.mywidget.PopView;
import intersky.mywidget.conturypick.PinyinUtil;

public class ConnectView extends PopView {

    public TextView tip;
    public XpxConnect xpxConnect;
    public Handler handler;
    public ConnectView(Context context, XpxConnect xpxConnect, Handler handler) {
        super(context);
        this.xpxConnect = xpxConnect;
        this.handler = handler;
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_finger_get,null);
        tip = mainView.findViewById(R.id.tip);
        close =mainView.findViewById(R.id.view_finger);
    }

    @Override
    public void destoryView() {

    }

    public void creatView(View location) {
        super.creatView(location);
        if(popupWindow != null)
        {
            popupWindow.setOnDismissListener(dismissListener);
        }
        updataConnect();
    }

    public void setText(String imf) {
        if(tip != null)
        tip.setText(imf);
    }

    public void hid()
    {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }

    public PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener()
    {

        @Override
        public void onDismiss() {
            Message msg = new Message();
            msg.obj = xpxConnect;
            msg.what = BlueToothHandler.CONNECT_DEVICE_DISMISS;
            if(handler != null)
                handler.sendMessage(msg);
        }
    };

    public void setTip(String persent)
    {
        tip.setText(context.getString(R.string.connect_start)+persent);
    }

    public void updataConnect()
    {
        Message msg = new Message();
        msg.obj = xpxConnect;
        msg.what = BlueToothHandler.CONNECT_DEVICE_UPDATA;
        if(handler != null)
        handler.sendMessage(msg);
    }
}
