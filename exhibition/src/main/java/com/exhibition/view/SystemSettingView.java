package com.exhibition.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exhibition.R;

import intersky.apputils.ApkUtils;
import intersky.apputils.AppUtils;
import intersky.mywidget.PopView;

public class SystemSettingView extends PopView {

    public TextView nowverson;
    public TextView upverson;
    public TextView btnupdate;
    public View hid;
    public SystemSettingView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_systemupdate,null);
        close = mainView.findViewById(R.id.view_register);
        hid = mainView.findViewById(R.id.hidinput);
        nowverson = mainView.findViewById(R.id.versionvalue);
        upverson = mainView.findViewById(R.id.updatevalue);
        btnupdate = mainView.findViewById(R.id.updatbtn);
        btnupdate.setOnClickListener(updataListener);
        hid.setOnClickListener(hidinputListener);
    }

    public void creatView(View location) {
        initData();
        super.creatView(location);
    }

    private void initData() {
        try {
            nowverson.setText(ApkUtils.getVersionName(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ExhibitionApplication.mApp.apk.exists())
        {
            upverson.setText(ApkUtils.apkInfo(ExhibitionApplication.mApp.apk.getPath(),context));
            btnupdate.setVisibility(View.VISIBLE);
        }
        else
        {
            upverson.setText(context.getString(R.string.systemupdate_none));
            btnupdate.setVisibility(View.INVISIBLE);
        }
    }

    private View.OnClickListener updataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            context.startActivity(ExhibitionApplication.mApp.fileUtils.openfile(ExhibitionApplication.mApp.apk));
        }
    };

    public View.OnClickListener hidinputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    };
}
