package com.accesscontrol.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.accesscontrol.R;

import org.w3c.dom.Text;

import intersky.mywidget.PopView;

public class SettingView extends PopView {

    public TextView baseTimeValue;
    public TextView baseLangueValue;
    public TextView baseSysSoundValue;
    public TextView baseKeySoundValue;
    public TextView baseIcValue;
    public TextView basePasswordValue;
    public TextView baseSuperValue;



    public SettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_setting,null);
    }
}
