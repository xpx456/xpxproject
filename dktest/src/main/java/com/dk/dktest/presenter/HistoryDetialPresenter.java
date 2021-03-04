package com.dk.dktest.presenter;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dk.dktest.R;
import com.dk.dktest.database.DBHelper;
import com.dk.dktest.entity.TestRecord;
import com.dk.dktest.handler.HistoryDetialHandler;
import com.dk.dktest.view.DkTestApplication;
import com.dk.dktest.view.activity.HistoryDetialActivity;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import xpx.com.toolbar.utils.ToolBarHelper;

public class HistoryDetialPresenter implements Presenter {

    public HistoryDetialActivity mHistoryDetialActivity;
    public HistoryDetialHandler mHistoryDetialHandler;

    public HistoryDetialPresenter(HistoryDetialActivity HistoryDetialActivity) {
        mHistoryDetialActivity = HistoryDetialActivity;
        mHistoryDetialHandler = new HistoryDetialHandler(HistoryDetialActivity);
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mHistoryDetialActivity, Color.argb(0, 255, 255, 255));
        mHistoryDetialActivity.setContentView(R.layout.activity_sport_detial);
        mHistoryDetialActivity.chartShow = mHistoryDetialActivity.findViewById(R.id.chart);
        mHistoryDetialActivity.chartShow.getSettings().setAllowFileAccess(true);
        mHistoryDetialActivity.chartShow.getSettings().setJavaScriptEnabled(true);
        mHistoryDetialActivity.chartShow.setWebViewClient(mWebViewClient);
        mHistoryDetialActivity.btnBack = mHistoryDetialActivity.findViewById(R.id.back);
        mHistoryDetialActivity.btnBack.setOnClickListener(backListener);
        mHistoryDetialActivity.chartShow.setVisibility(View.VISIBLE);
        mHistoryDetialActivity.chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        mHistoryDetialActivity.title = mHistoryDetialActivity.findViewById(R.id.main_title);
        mHistoryDetialActivity.testRecord = mHistoryDetialActivity.getIntent().getParcelableExtra("record");
        mHistoryDetialActivity.title.setText(mHistoryDetialActivity.testRecord.name);
        mHistoryDetialActivity.mToolBarHelper.hidToolbar(mHistoryDetialActivity, (RelativeLayout) mHistoryDetialActivity.findViewById(R.id.buttomaciton));
        mHistoryDetialActivity.measureStatubar(mHistoryDetialActivity, (RelativeLayout) mHistoryDetialActivity.findViewById(R.id.stutebar));

        mHistoryDetialHandler.sendEmptyMessageDelayed(HistoryDetialHandler.UPDATA_DATA,300);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
        mHistoryDetialActivity.chartShow.destroy();
    }




    public WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // 当有新连接时使用当前的webview进行显示
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        // 开始加载网页时要做的工作
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.INVISIBLE);
            mHistoryDetialActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            mHistoryDetialActivity.waitDialog.hide();
            if (mHistoryDetialActivity.isFirst == false) {
                mHistoryDetialActivity.isFirst = true;
                //mChartActivity.waitDialog.show();
            }
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mHistoryDetialActivity.waitDialog.hide();
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mHistoryDetialActivity.finish();
        }
    };

    public void loadChart()
    {
        String url = "javascript:createChart('orther'," + mHistoryDetialActivity.testRecord.data+");";
        mHistoryDetialActivity.chartShow.loadUrl(url);
    }
}
