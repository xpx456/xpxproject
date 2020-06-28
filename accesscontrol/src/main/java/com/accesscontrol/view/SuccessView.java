package com.accesscontrol.view;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.accesscontrol.R;

import intersky.apputils.AppUtils;
import intersky.mywidget.PopView;


public class SuccessView extends PopView {

    public Context context;
    public View mainView;
    public TextView user;
    public TextView type;
    public TextView cast;
    public TextView last;
    public PopupWindow popupWindow;


    public SuccessView(Context context) {
        super(context);
    }
}



