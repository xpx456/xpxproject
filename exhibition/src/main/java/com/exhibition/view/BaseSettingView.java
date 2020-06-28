package com.exhibition.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;
import com.exhibition.entity.MyPageListData;
import com.exhibition.view.adapter.QueryListAdapter;

import intersky.apputils.TimeUtils;
import intersky.mywidget.PopView;

public class BaseSettingView extends PopView {

    public EditText name;



    public BaseSettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_basesetting,null);
    }

    public void creatView(View location) {
        super.creatView(location);
    }

}
