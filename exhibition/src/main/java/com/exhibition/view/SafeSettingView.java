package com.exhibition.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.exhibition.R;

import intersky.mywidget.PopView;

public class SafeSettingView extends PopView {

    public EditText name;



    public SafeSettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_systemupdate,null);
    }

    public void creatView(View location) {
        super.creatView(location);
    }

}
