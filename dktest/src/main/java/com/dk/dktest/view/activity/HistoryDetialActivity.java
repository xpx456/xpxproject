package com.dk.dktest.view.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dktest.entity.TestRecord;
import com.dk.dktest.presenter.HistoryDetialPresenter;

import intersky.appbase.BaseActivity;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;

public class HistoryDetialActivity extends BaseActivity {

    public WebView chartShow;
    public ImageView btnBack;
    public TestRecord testRecord;
    public TextView title;
    public boolean isFirst = false;
    public HistoryDetialPresenter mHistoryDetialPresenter = new HistoryDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mHistoryDetialPresenter.Destroy();
        super.onDestroy();
    }
}
