package com.iccard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import intersky.mywidget.PopView;


public class ICcardView extends PopView {

    public TextView tip;
    public ICcardView(Context context) {
        super(context);
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
