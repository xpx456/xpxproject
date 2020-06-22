package com.finger.view;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.finger.R;

import intersky.apputils.AppUtils;

public class GetFingerView {

    public Context context;
    public TextView tip;
    public View mainView;
    public PopupWindow popupWindow;
    public GetFingerView(Context context) {
        this.context = context;
    }

    public AppUtils.InitView initView = new AppUtils.InitView()
    {

        @Override
        public void initView(View view) {
            mainView = view;
            tip = mainView.findViewById(R.id.tip);
        }
    };

    public AppUtils.DestoryView destoryView = new AppUtils.DestoryView() {
        @Override
        public void destoryView(View view) {

        }
    };

    public void creatView(View location) {
        popupWindow = AppUtils.creatPopView(context, R.layout.view_finger_get,R.id.view_finger,location,initView,destoryView);
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
}
