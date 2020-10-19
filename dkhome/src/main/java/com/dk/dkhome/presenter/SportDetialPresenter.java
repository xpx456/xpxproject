package com.dk.dkhome.presenter;


import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dk.dkhome.EchartOptionUtil;
import com.dk.dkhome.R;
import com.dk.dkhome.handler.SportDetialHandler;
import com.dk.dkhome.view.activity.SportDetialActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SportDetialPresenter implements Presenter {

    public SportDetialActivity mSportDetialActivity;
    public SportDetialHandler mSportDetialHandler;

    public SportDetialPresenter(SportDetialActivity SportDetialActivity) {
        mSportDetialActivity = SportDetialActivity;
        mSportDetialHandler = new SportDetialHandler(SportDetialActivity);
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSportDetialActivity, Color.argb(0, 255, 255, 255));
        mSportDetialActivity.setContentView(R.layout.activity_sport_detial);
        mSportDetialActivity.chart = mSportDetialActivity.findViewById(R.id.chart);
        mSportDetialActivity.chart2 = mSportDetialActivity.findViewById(R.id.chart1);
        mSportDetialActivity.chart.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                refreshLineChart();
            }
        });
        mSportDetialActivity.chart2.loadUrl("file:///android_asset/myecharts.html");
        mSportDetialActivity.mToolBarHelper.hidToolbar(mSportDetialActivity, (RelativeLayout) mSportDetialActivity.findViewById(R.id.buttomaciton));
        mSportDetialActivity.measureStatubar(mSportDetialActivity, (RelativeLayout) mSportDetialActivity.findViewById(R.id.stutebar));
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
        mSportDetialActivity.chart.destroy();
    }

    private void refreshLineChart(){
        Object[] x = new Object[]{
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };
        Object[] y = new Object[]{
                0, 2, 4, 8, 3, 0, 3, 6, 10, 7, 0
        };
        mSportDetialActivity.chart.refreshEchartsWithOption(EchartOptionUtil.getLineChartOptions(x, y));
    }
}
