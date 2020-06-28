package com.exhibition.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.exhibition.R;

import intersky.mywidget.PopView;

public class SystemSettingView extends PopView {

    public EditText name;



    public SystemSettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_safesetting,null);
    }

    public void creatView(View location) {
        super.creatView(location);
    }

}
