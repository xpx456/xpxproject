package com.dk.dktest.view.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dktest.EchartView;
import com.dk.dktest.presenter.SportDetialPresenter;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;

public class SportDetialActivity extends BaseActivity {

    public WebView chartShow;
    public ImageView btnBack;
    public TextView start;
    public TextView stop;
    public TextView add;
    public TextView del;
    public ObjectData option;
    public ArrayData x;
    public ArrayData y;
    public double time = 0;
    public boolean isFirst = false;
    public TextView electvalue;
    public TextView cyclevalue;
    public TextView leavelvalue;
    public TextView save;
    public TextView wran;
    public boolean isstart = false;
    public SportDetialPresenter mSportDetialPresenter = new SportDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSportDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSportDetialPresenter.Destroy();
        super.onDestroy();
    }
}
